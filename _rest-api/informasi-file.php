<?php
$dir = "./images/";

$arr = [];

if (isset($_FILES['image']['name'])) {
    $file_name = basename($_FILES['image']['name']);

    $extension = strtolower(pathinfo($file_name, PATHINFO_EXTENSION));

    if ($extension == 'png' || $extension == 'jpg' || $extension == 'jpeg') {
        if ($_FILES["image"]["size"] < 4000001) {
            $file = $dir . $file_name;
            if (move_uploaded_file($_FILES['image']['tmp_name'], $file)) {
                $arr = array(
                    'success' => 1,
                    'message' => "File Uploaded",
                    'file_name' => $file_name
                );
            } else {
                $arr = array(
                    'success' => 0,
                    'error' => "Something Went Wrong Please Retry",
                    'file_name' => $file_name
                );
            }
        } else {
            $arr = array(
                'success' => 0,
                'error' => "File size cant exceed 4 MB"
            );
        }
    } else {
        $arr = array(
            'success' => 0,
            'error' => "Only .png, .jpg and .jpeg format are accepted"
        );
    }
} else {
    $arr = array(
        'success' => 1,
        'message' => "Please try Post Method"
    );
}

echo json_encode($arr);
