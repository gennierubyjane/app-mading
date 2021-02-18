<?php
include "+koneksi.php";

$sql = mysqli_query($db, "SELECT * FROM kategori") or die($db->error);

$data = [];

$no = 0;

if (mysqli_num_rows($sql) > 0) {
    while ($fetch = mysqli_fetch_array($sql)) {
        $data[$no]['id'] = $fetch['id'];
        $data[$no]['category'] = $fetch['nama'];

        $no++;
    }
}

echo json_encode($data);
