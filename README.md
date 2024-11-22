# Space Invaders

## Overview
**Space Invaders** adalah Space Invaders adalah implementasi modern dari game arkade klasik yang membawa Anda kembali ke nostalgia era 80-an. Anda akan mengontrol pesawat luar angkasa dan berusaha untuk bertahan hidup dari serangan alien.


## 🎯 **Fitur Utama**

- **Kontrol Intuitif**:
  - Gunakan tombol panah kiri/kanan atau tombol `A`/`D` untuk menggerakkan pesawat.
  - Tembak musuh dengan menekan tombol `Spasi`.

- **Gameplay Dinamis**:
  - Setiap musuh yang dihancurkan memberikan 10 skor.
  - Hindari peluru musuh untuk menjaga nyawa tetap utuh.
  - Game berakhir jika nyawa habis atau pemain mencapai skor tertentu.

- **Visual dan Efek Animasi**:
  - Latar belakang penuh bintang yang bergerak untuk menciptakan suasana luar angkasa.
  - Animasi ledakan setiap kali musuh dihancurkan atau pesawat terkena peluru.

- **Tampilan Layar Akhir**:
  - Pesan **Kemenangan**: Muncul ketika pemain mencapai skor kemenangan.
  - Pesan **Kekalahan**: Muncul ketika pemain kehilangan semua nyawa.

---

## 📂 **Struktur Proyek**

Proyek ini memiliki struktur direktori berikut:

```
.
├── src/
│   └── SpaceInvadersGame.java
├── resources/
│   └── images/
│       ├── Spaceship.png
│       ├── Enemy.png
│       ├── explosion1.png
│       ├── explosion2.png
│       ├── explosion3.png
│       └── explosion4.png
├── README.md
```



- **`src/`**: Berisi kode sumber utama game.
- **`resources/`**:
  - **`images/`**: Berisi gambar seperti pesawat, musuh, dan animasi ledakan.
- **`README.md`**: Dokumentasi proyek.

---

## 🛠️ **Instalasi dan Cara Menjalankan**

### Persyaratan
- **JDK** versi 8 atau lebih baru.
- *IDE* seperti IntelliJ IDEA, Eclipse, atau Apache Netbeans.

### Langkah-Langkah Instalasi

1. **Clone Repositori**:
   Jalankan perintah berikut di terminal untuk mengunduh source code:
   ```bash
   git clone https://github.com/FajrulLent/Space-Invaders.git
2. **Buka Proyek**:
Impor direktori proyek ke IDE sebagai proyek Java biasa.

3. **Jalankan Game**:
   - Temukan file `SpaceInvadersGame.java`
   - Jalankan file tersebut.

---

## 🎮 **Panduan Bermain**

1. **Tujuan**:
   - Kumpulkan poin sebanyak 200 dengan menembak musuh (10 poin per musuh).
   - Hindari peluru musuh agar nyawa tidak habis (5 nyawa).

2. **Kontrol Pemain**:
   - Gerak ke kiri: Tekan `A` atau tombol panah kiri.
   - Gerak ke kanan: Tekan `D` atau tombol panah kanan.
   - Tembak peluru: Tekan tombol `Spasi`.

3. **Strategi**:
   - Fokus pada musuh terdekat untuk menghindari serangan bertubi-tubi.
   - Hindari peluru musuh sambil terus bergerak.
  
---  
  
## 📜 **Penjelasan Kode**

- **`Player`**: 
  - Mengontrol pesawat pemain.
  - Bergerak ke kiri/kanan berdasarkan input.
  - Menembakkan peluru ke arah musuh.

- **`Enemy`**: 
  - Musuh yang muncul secara acak di layar.
  - Musuh menembakkan peluru ke pemain.

- **`Bullet`**: 
  - Peluru dapat ditembakkan oleh pemain atau musuh.

- **`Explosion`**: 
  - Efek visual saat musuh atau pemain terkena tembakan.
  - Animasi menggunakan beberapa frame gambar.

- **`Star`**: 
  - Elemen visual untuk menciptakan efek latar belakang luar angkasa

---
