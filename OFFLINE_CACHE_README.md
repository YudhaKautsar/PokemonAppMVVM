# Pokemon App - Offline Caching Implementation

## Overview
Aplikasi Pokemon sekarang mendukung offline caching menggunakan SQLite database melalui Room. User dapat melihat list Pokemon yang sudah pernah dimuat sebelumnya meskipun tanpa koneksi internet.

## Fitur Offline Caching

### 1. Cache-First Strategy
- Aplikasi akan mencoba mengambil data dari cache lokal terlebih dahulu
- Jika data tersedia di cache, akan langsung ditampilkan tanpa perlu internet
- Jika cache kosong, baru akan mencoba mengambil dari API
- Jika API gagal, akan fallback ke cache sebagai backup

### 2. Automatic Data Persistence
- Setiap kali data Pokemon berhasil dimuat dari API, otomatis disimpan ke database lokal
- Data tersimpan termasuk: ID, nama, URL gambar, dan offset untuk pagination
- Database menggunakan Room dengan migration support

### 3. Offline Search
- Fitur pencarian tetap berfungsi offline menggunakan data yang tersimpan di cache
- Search dilakukan di database lokal dengan query LIKE
- Jika tidak ada hasil di cache, akan mencoba API (jika ada internet)

### 4. Pagination Support
- Pagination tetap berfungsi dengan data cache
- Setiap batch data (10 Pokemon per load) disimpan dengan offset tracking
- Load more akan mengambil data berikutnya dari cache atau API

## Technical Implementation

### Database Schema
```sql
CREATE TABLE pokemon (
    id INTEGER PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    imageUrl TEXT NOT NULL,
    offset INTEGER NOT NULL DEFAULT 0
);
```

### Key Components

1. **PokemonEntity.kt** - Room entity untuk Pokemon
2. **PokemonDao.kt** - Data Access Object untuk operasi database
3. **AppDatabase.kt** - Room database configuration dengan migration
4. **PokemonRepository.kt** - Repository dengan cache-first logic
5. **HomeViewModel.kt** - ViewModel yang menggunakan AndroidViewModel untuk context

### Cache Strategy Flow

```
1. User request data
   ↓
2. Check local cache first
   ↓
3. If cache has data → Return cached data
   ↓
4. If cache empty → Try API call
   ↓
5. If API success → Save to cache + Return API data
   ↓
6. If API fails → Return cached data as fallback
   ↓
7. If no cache and no internet → Show empty state
```

## Benefits

- **Faster Loading**: Data dari cache dimuat lebih cepat
- **Offline Access**: User tetap bisa melihat Pokemon yang sudah pernah dimuat
- **Data Persistence**: Data tersimpan permanen sampai app di-uninstall
- **Reduced API Calls**: Mengurangi penggunaan bandwidth dan server load
- **Better UX**: User experience yang lebih smooth dan reliable

## Usage

1. **First Time**: App akan load data dari API dan menyimpannya
2. **Subsequent Loads**: App akan menampilkan data cache terlebih dahulu
3. **Offline Mode**: App tetap menampilkan data yang sudah tersimpan
4. **Search Offline**: Pencarian tetap berfungsi dengan data cache
5. **Pagination Offline**: Load more tetap berfungsi dengan data cache

## Database Migration

Database version telah diupgrade dari v1 ke v2 dengan menambahkan tabel Pokemon. Migration otomatis akan dijalankan saat pertama kali membuka app setelah update.