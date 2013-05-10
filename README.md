# WELCOME TO MANDO

Latest result:

![Mando](http://mufid.github.io/images/post/mando.png)

# PANDUAN KOMPILASI

1. Download [ABS](https://github.com/JakeWharton/ActionBarSherlock/archive/4.2.0.zip)
2. Extract folder library **folder yang sama dengan folder mando**. Misal: (bingung? Lihat `project.properties` di folder Mando.

		~/
          mando/
		    src
		    bin
		    libs
		    res
		    ....
		  library/
		    src
		    gen
		    bin

3. Import project `library` tersebut ke dalam eclipse
4. Clean, Build, Build, Clean, Build, Build, Deploy ke device

# PANDUAN NGODING

1. Branch `nama-dev` dari master saat ini
2. Push
3. Biar Mufid yang merge

# DAFTAR TUGAS WBS

Ubah baris, tambah menjadi [OK] jika sudah selesai

[x] Semua iterasi 1
[ ] Abis install / pertama kali run; langsung ada panduan ganti PIN
[ ] Implementasi callback
[ ] Implementasi lokasi 
[ ] Implementasi location name
[ ] Implementasi remote wipe
[ ] Implementasi hapus sms yang udah dikirim
[ ] Implementasi hapus semua sms, yang ada di kartu SD, dan backup
[ ] Konek ke server SMTP
[ ] Rekam suara, masukin ke file
[ ] Kirim melalui sistem SMTP
[ ] Konfigurasi server SMTP

TAMBAH WBS sesuai kebutuhan
