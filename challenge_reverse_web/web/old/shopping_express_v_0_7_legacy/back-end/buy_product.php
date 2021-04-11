<?php

if(!isset($_POST['id']) || !isset($_POST['quantity']) || !isset($_POST['price']))
{
    echo 'Bad request';
    exit();
}

$product_id = $_POST['id'];
$quantity = $_POST['quantity'];
$price = $_POST['price'];

if($quantity == 0)
{
    echo 'So... You called me to tell me you did not want to buy anything ?';
}

if($quantity < 0)
{
    echo 'Humm... Are you making a delivery ?';
    exit();
}

if($price < 0)
{
    echo 'So how do you want to proceed to send the money to me ?';
}

if($price > 0)
{
    echo 'Payment module in maintenance';
    exit();
}

if(floatval($app_version) < 1.0 || $product_id != 4)
{
    echo 'Delivery impossible';
    exit();
}

$product_file_path = $dir_path . "products/product_" . str_pad($product_id, 4, "0", STR_PAD_LEFT) . ".txt";
$product_content = file_get_contents($product_file_path);
echo "file:\n" . $product_content;

?>
