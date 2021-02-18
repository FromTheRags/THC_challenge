<?php

if(!isset($_GET['id']) || !isset($_GET['quantity']))
{
    echo 'Bad request';
    exit();
}

$product_id = $_GET['id'];
$quantity = $_GET['quantity'];

$stm = $pdo->prepare("SELECT Price, QuantityAvailable FROM Products WHERE ProductId = :id");
$stm->bindParam(":id", $product_id, PDO::PARAM_INT);
$stm->execute();

if($stm->rowCount() == 0)
{
    echo 'Wrong product id';
    exit();
}

$row = $stm->fetch(PDO::FETCH_ASSOC);

if($row['QuantityAvailable'] == 0)
{
    echo 'Product out of stock';
    exit();
}

if($row['Price'] > 0)
{
    echo 'Payment module in maintenance';
    exit();
}

if($product_id != 4)
{
    echo 'Delivery impossible';
    exit();
}

$product_file_path = "../products/product_" . str_pad($product_id, 4, "0", STR_PAD_LEFT) . ".txt";
$product_content = file_get_contents($product_file_path);
echo $product_content;

?>
