<?php
include "+koneksi.php";

$cari = $_GET['cari'];

if($cari != '') {
    $sql = mysqli_query($db, "SELECT * FROM informasi WHERE judul like ('%$cari%') OR detail like ('%$cari%') ORDER BY id DESC") or die($db->error);
} else {
    $sql = mysqli_query($db, "SELECT * FROM informasi ORDER BY id DESC") or die($db->error);
}

$data = [];

$no = 0;

if (mysqli_num_rows($sql) > 0) {
    while ($fetch = mysqli_fetch_array($sql)) {
        $data[$no]['id'] = $fetch['id'];
        $data[$no]['judul'] = $fetch['judul'];
        $data[$no]['diunggah_oleh'] = $fetch['diunggah_oleh'];
        $data[$no]['waktu'] = $fetch['waktu'];
        $data[$no]['detail'] = $fetch['detail'];
        $data[$no]['image'] = $fetch['image'];

        $no++;
    }
}

echo json_encode($data);