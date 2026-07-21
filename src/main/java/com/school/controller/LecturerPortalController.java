package com.school.controller;

import com.school.entity.*;
import com.school.repository.*;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/lecturer")
public class LecturerPortalController {
 private final LecturerRepository lecturers;
 private final UserRepository users;
 private final UnitRepository units;
 private final StudentRepository students;
 private final AnnouncementRepository announcements;
 private final AttendanceRepository attendance;
 private final LecturerProfileImageRepository profileImages;
 private final EnrollmentRepository enrollments;

 public LecturerPortalController(LecturerRepository lecturers,UserRepository users,UnitRepository units,StudentRepository students,AnnouncementRepository announcements,AttendanceRepository attendance,LecturerProfileImageRepository profileImages,EnrollmentRepository enrollments){
  this.lecturers=lecturers;
  this.users=users;
  this.units=units;
  this.students=students;
  this.announcements=announcements;
  this.attendance=attendance;
  this.profileImages=profileImages;
  this.enrollments=enrollments;
 }

 private Lecturer lecturer(Authentication auth){
  return lecturers.findByUserUsername(auth.getName()).orElseGet(()->{
   User user=users.findByUsername(auth.getName()).orElseThrow();
   Lecturer lecturer=new Lecturer("LEC/2026/001",user.getFullName(),user.getEmail());
   lecturer.setUser(user);
   lecturer.setPhone("Not provided");
   return lecturers.save(lecturer);
  });
 }

 private List<Unit> assigned(Lecturer lecturer){
  List<Unit> list=units.findByLecturer(lecturer);
  if(list.isEmpty()){
   List<Unit> available=units.findAll().stream().filter(unit->unit.getLecturer()==null).limit(2).toList();
   if(!available.isEmpty()){
    available.forEach(unit->unit.setLecturer(lecturer));
    units.saveAll(available);
   }else{
    Unit one=new Unit("LEC 210","Teaching Practice I");
    one.setLecturer(lecturer);
    Unit two=new Unit("LEC 220","Teaching Practice II");
    two.setLecturer(lecturer);
    units.saveAll(List.of(one,two));
   }
   list=units.findByLecturer(lecturer);
  }
  return list;
 }

 private String page(Model model,Authentication auth,String active){
  Lecturer lecturer=lecturer(auth);
  List<Unit> unitList=assigned(lecturer);
  Map<Long,Long> unitStudentCounts=new HashMap<>();
  for(Unit u:unitList){
   unitStudentCounts.put(u.getId(),enrollments.countByUnit(u));
  }
  model.addAttribute("lecturer",lecturer);
  model.addAttribute("hasLecturerImage",profileImages.existsByLecturer(lecturer));
  model.addAttribute("active",active);
  model.addAttribute("units",unitList);
  model.addAttribute("unitStudentCounts",unitStudentCounts);
  model.addAttribute("students",students.findAll());
  model.addAttribute("announcements",announcements.findAll());
  return "lecturer/portal";
 }

 @GetMapping("/dashboard") public String dashboard(Model m,Authentication a){return page(m,a,"dashboard");}
 @GetMapping("/profile") public String profile(Model m,Authentication a){return page(m,a,"profile");}

 @PostMapping("/profile")
 @Transactional
 public String profileSave(Authentication a,@RequestParam String email,@RequestParam String phone,@RequestParam(required=false) MultipartFile profileImage){
  Lecturer lecturer=lecturer(a);
  lecturer.setEmail(email);
  lecturer.setPhone(phone);
  if(profileImage!=null&&!profileImage.isEmpty()){
   String type=profileImage.getContentType();
   if(type==null||!type.startsWith("image/")||profileImage.getSize()>2_000_000){
    return "redirect:/lecturer/profile?imageError";
   }
   try{
    LecturerProfileImage image=profileImages.findByLecturer(lecturer).orElse(new LecturerProfileImage(lecturer,profileImage.getBytes(),type));
    image.replace(profileImage.getBytes(),type);
    profileImages.save(image);
   }catch(Exception exception){
    return "redirect:/lecturer/profile?imageError";
   }
  }
  lecturers.save(lecturer);
  return "redirect:/lecturer/profile?updated";
 }

 @GetMapping("/profile-picture")
 public ResponseEntity<byte[]> profilePicture(Authentication a){
  LecturerProfileImage image=profileImages.findByLecturer(lecturer(a)).orElse(null);
  if(image==null) return ResponseEntity.notFound().build();
  MediaType type=image.getContentType()==null?MediaType.IMAGE_JPEG:MediaType.parseMediaType(image.getContentType());
  return ResponseEntity.ok().contentType(type).cacheControl(CacheControl.noCache()).body(image.getData());
 }

 @GetMapping("/units") public String myUnits(Model m,Authentication a){return page(m,a,"units");}
 @GetMapping("/timetable") public String timetable(Model m,Authentication a){return page(m,a,"timetable");}
 @GetMapping("/students") public String studentLists(Model m,Authentication a){return page(m,a,"students");}

 @GetMapping("/attendance")
 public String attendance(Model m,Authentication a,@RequestParam(required=false) Long unitId){
  page(m,a,"attendance");
  @SuppressWarnings("unchecked")
  List<Unit> unitList=(List<Unit>)m.asMap().get("units");
  if(unitList==null||unitList.isEmpty()){
   m.addAttribute("selectedUnit",null);
   m.addAttribute("attendanceRecords",Collections.emptyList());
   m.addAttribute("todayPresentStudentIds",Collections.emptySet());
   m.addAttribute("hasTodayAttendance",false);
   return "lecturer/portal";
  }
  Unit selected=unitId==null?unitList.get(0):unitList.stream().filter(x->x.getId().equals(unitId)).findFirst().orElse(unitList.get(0));
  m.addAttribute("selectedUnit",selected);
  m.addAttribute("attendanceRecords",attendance.findByUnitOrderByAttendanceDateDesc(selected));

  List<Attendance> todayRecords=attendance.findByUnitAndAttendanceDate(selected,LocalDate.now());
  Set<Long> todayPresentStudentIds=new HashSet<>();
  if(todayRecords!=null){
   for(Attendance att:todayRecords){
    if(att!=null&&att.isPresent()&&att.getStudent()!=null){
     todayPresentStudentIds.add(att.getStudent().getId());
    }
   }
  }
  m.addAttribute("todayPresentStudentIds",todayPresentStudentIds);
  m.addAttribute("hasTodayAttendance",todayRecords!=null&&!todayRecords.isEmpty());
  return "lecturer/portal";
 }

 @PostMapping("/attendance")
 @Transactional
 public String saveAttendance(Authentication a,@RequestParam Long unitId,@RequestParam(required=false) List<Long> present){
  Lecturer lecturer=lecturer(a);
  List<Unit> assignedUnits=assigned(lecturer);
  if(assignedUnits==null||assignedUnits.isEmpty()){
   return "redirect:/lecturer/attendance";
  }
  Unit unit=assignedUnits.stream().filter(x->x.getId().equals(unitId)).findFirst().orElse(assignedUnits.get(0));
  attendance.deleteByUnitAndAttendanceDate(unit,LocalDate.now());
  List<Student> allStudents=students.findAll();
  Set<Long> presentIds=present!=null?new HashSet<>(present):Collections.emptySet();
  for(Student student:allStudents){
   if(student!=null){
    attendance.save(new Attendance(student,unit,LocalDate.now(),presentIds.contains(student.getId())));
   }
  }
  return "redirect:/lecturer/attendance?unitId="+unit.getId()+"&saved";
 }

 @GetMapping("/notifications")
 public String notifications(Model m,Authentication a){return page(m,a,"notifications");}
}
