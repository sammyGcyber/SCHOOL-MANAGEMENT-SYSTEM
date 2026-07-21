document.querySelector('.theme-toggle')?.addEventListener('click', () => document.body.classList.toggle('dark'));

document.querySelector('.sidebar-toggle')?.addEventListener('click', () => {
  const collapsed = document.body.classList.toggle('sidebar-collapsed');
  document.querySelector('.sidebar-toggle')?.setAttribute('aria-expanded', String(!collapsed));
});

const imageInput = document.querySelector('input[name="profileImage"]');
imageInput?.addEventListener('change', () => {
  const file = imageInput.files?.[0];
  if (!file || !file.type.startsWith('image/')) {
    imageInput.value = '';
    return;
  }

  const source = new Image();
  source.onload = () => {
    const overlay = document.createElement('div');
    overlay.className = 'photo-cropper';
    overlay.innerHTML = `
      <div class="photo-cropper-card">
        <h2>Position your profile photo</h2>
        <p>Drag the image to position it, then use the zoom control.</p>
        <canvas width="400" height="400"></canvas>
        <input type="range" min="100" max="250" value="100" aria-label="Photo zoom">
        <div class="crop-actions">
          <button type="button" class="crop-cancel">Cancel</button>
          <button type="button" class="primary crop-save">Use photo</button>
        </div>
      </div>
    `;
    document.body.append(overlay);

    const canvas = overlay.querySelector('canvas');
    const ctx = canvas.getContext('2d');
    const slider = overlay.querySelector('input');
    let offsetX = 0, offsetY = 0, dragging = false, lastX = 0, lastY = 0;

    const draw = () => {
      const base = Math.max(400 / source.width, 400 / source.height);
      const scale = base * Number(slider.value) / 100;
      const width = source.width * scale, height = source.height * scale;
      const left = Math.min(0, Math.max(400 - width, (400 - width) / 2 + offsetX));
      const top = Math.min(0, Math.max(400 - height, (400 - height) / 2 + offsetY));
      ctx.clearRect(0, 0, 400, 400);
      ctx.drawImage(source, left, top, width, height);
    };

    draw();
    slider.addEventListener('input', draw);

    canvas.addEventListener('pointerdown', (e) => {
      dragging = true;
      lastX = e.clientX;
      lastY = e.clientY;
      canvas.setPointerCapture(e.pointerId);
    });

    canvas.addEventListener('pointermove', (e) => {
      if (!dragging) return;
      const ratio = 400 / canvas.getBoundingClientRect().width;
      offsetX += (e.clientX - lastX) * ratio;
      offsetY += (e.clientY - lastY) * ratio;
      lastX = e.clientX;
      lastY = e.clientY;
      draw();
    });

    canvas.addEventListener('pointerup', () => { dragging = false; });

    overlay.querySelector('.crop-cancel').addEventListener('click', () => {
      imageInput.value = '';
      overlay.remove();
    });

    overlay.querySelector('.crop-save').addEventListener('click', () => {
      canvas.toBlob((blob) => {
        if (!blob) return;
        const cropped = new File([blob], 'profile-photo.jpg', { type: 'image/jpeg' });
        const data = new DataTransfer();
        data.items.add(cropped);
        imageInput.files = data.files;

        const photoUrl = URL.createObjectURL(cropped);
        document.querySelectorAll('.student-avatar img, .lecturer-avatar img, .profile-big img').forEach(img => {
          img.src = photoUrl;
        });

        overlay.remove();
      }, 'image/jpeg', 0.85);
    });
  };
  source.src = URL.createObjectURL(file);
});

const adminSide = document.querySelector('.admin-side');
if (adminSide) {
  const adminPhoto = url => {
    document.querySelectorAll('.student-avatar, .profile-big').forEach(target => {
      const img = document.createElement('img');
      img.src = url;
      img.alt = 'Admin profile photo';
      target.replaceChildren(img);
    });
  };
  fetch(`/admin/profile-picture?time=${Date.now()}`).then(response => response.ok ? response.blob() : null).then(image => {
    if (image) adminPhoto(URL.createObjectURL(image));
  }).catch(()=>{});
}

