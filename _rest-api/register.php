<?php
include "+koneksi.php";

$name = $_POST['name'];
$username = $_POST['username'];
$password = md5($_POST['password']);
$level = "pengguna";

$data = [];

$sqlCekUsername = mysqli_query($db, "SELECT * FROM user WHERE username = '$username'") or die($db->error);

if (mysqli_num_rows($sqlCekUsername) > 0) {
    $data["success"] = 0;
    $data["error"] = 'Gagal daftar, username telah digunakan';
} else { 
    mysqli_query($db, "INSERT INTO `user` (`name`, `username`, `password`, `level`) VALUES ('$name', '$username', '$password', '$level')") or die($db->error);
    
    $data["success"] = 1;
}
echo json_encode($data);