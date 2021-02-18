<?php
include "+koneksi.php";

$data = [];

$id = $_POST['id'];

mysqli_query($db, "DELETE FROM `informasi` WHERE id = '$id'") or die($db->error);

$data["success"] = 1;

echo json_encode($data);