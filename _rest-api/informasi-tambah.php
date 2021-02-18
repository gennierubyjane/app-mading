<?php
include "+koneksi.php";

$data = [];

$judul = $_POST['judul'];
$detail = $_POST['detail'];
$waktu = date('d M Y H:i', time() + (7 * 3600));
$diunggah_oleh = $_POST['diunggah_oleh'];
$image = $_POST['image'] . '.png';

mysqli_query($db, "INSERT INTO `informasi`(`judul`, `detail`, `waktu`, `diunggah_oleh`, `image`) VALUES ('$judul', '$detail', '$waktu', '$diunggah_oleh', '$image')") or die($db->error);

$data["success"] = 1;

echo json_encode($data);