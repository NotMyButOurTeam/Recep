# Recep 🍽️

Recep adalah aplikasi mobile berbasis Android untuk berbagi dan menelusuri resep makanan dan minuman dari seluruh dunia. Dengan antarmuka yang sederhana dan interaktif, Recep memungkinkan pengguna untuk tidak hanya mencari dan membaca resep, tetapi juga mengunggah dan menyunting resep mereka sendiri.

## 📱 Tentang Aplikasi

Aplikasi ini dikembangkan sebagai bagian dari tugas besar mata kuliah **Pemrograman Bergerak** pada Semester Genap 2025 di **Universitas Mataram**.

### Fitur Utama
- 🔍 Mencari dan melihat resep masakan/minuman
- ➕ Mengunggah dan menyunting resep pribadi
- ⭐ Menandai resep favorit dengan fitur bookmark
- 📤📥 Impor dan ekspor bookmark resep
- 📲 Akses offline untuk resep yang sudah diunduh

## 🎯 Tujuan Pengembangan

- Menyediakan platform pencarian dan penyebaran resep berbasis mobile.
- Meningkatkan pengalaman pengguna dengan antarmuka yang bersih, minim iklan, dan mudah dinavigasi.
- Memungkinkan kontribusi resep oleh pengguna sehingga koleksi resep menjadi lebih beragam.

## 📦 Struktur Proyek
'''
Recep/
├── java/com/recep/recep/
│ ├── components/ # Komponen turunan Android (custom views)
│ ├── data/ # Data class untuk model resep
│ ├── database/ # Akses ke database lokal/online
│ └── utils/ # Kelas utilitas (mis. pengecekan koneksi)
├── res/
│ ├── drawable/ # Icon dan gambar (.xml)
│ ├── layout/ # Layout tampilan UI (activity_, fragment_, item_)
│ ├── menu/ # Menu atas (top_) dan bawah (bottom_)
│ └── values/ # Strings, colors, themes'''


## 🧩 Tampilan Antarmuka

- **Beranda**: Daftar 8 resep teratas + search bar
- **Detail Resep**: Gambar makanan, bahan, cara masak, tombol bookmark/edit
- **Unggah Resep**: Form input lengkap untuk resep baru
- **Sunting Resep**: Edit nama, deskripsi, bahan, dan langkah
- **Bookmarks**: Daftar resep favorit + fitur impor/ekspor

## 🛠️ Teknologi

- **Platform**: Android
- **Bahasa**: Kotlin/Java
- **Database**: Room (Offline), Firebase/Cloud (opsional)
- **Desain**: XML Layout + Material Components

## 🔗 Link Terkait

- 📂 Source Code: [https://github.com/NotMyButOurTeam/Recep](https://github.com/NotMyButOurTeam/Recep)

## 👨‍💻 Kontributor

- Muhammad Fauzan Aqidah (F1D022144)  
- Muzayan Rozaan (F1D02310018)  
- Dzakanov Inshoofi (F1D02310110)  

## 📌 Lisensi

Proyek ini dikembangkan sebagai tugas akademik dan **tidak untuk tujuan komersial**. Silakan gunakan sebagai referensi edukatif.

---

