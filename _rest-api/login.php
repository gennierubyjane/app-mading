<?php
include "+koneksi.php";

$data = [];

$username = $_POST['username'];
$password = $_POST['password'];

$sqlm = mysqli_query($db, "SELECT * FROM user WHERE username = '$username' AND password = md5('$password')") or die($db->error);
$rm = mysqli_fetch_array($sqlm);
$row = mysqli_num_rows($sqlm);
if ($row > 0) {
    $data["success"] = 1;
    $data["data"]["id"] = $rm["id"];
    $data["data"]["name"] = $rm["name"];
    $data["data"]["username"] = $rm["username"];
    $data["data"]["level"] = $rm["level"];
} else {
    $data["success"] = 0;
    $data["data"] = null;
    $data["request"]['method'] = $_SERVER['REQUEST_METHOD'];
    $data["request"]['username'] = $username;
    $data["request"]['password'] = $password;
}

echo json_encode($data);
