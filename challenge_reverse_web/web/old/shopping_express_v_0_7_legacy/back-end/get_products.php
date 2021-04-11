<?php

$stm = $pdo->query("SELECT ProductId, Name, Description, Price, QuantityAvailable FROM Products");

if($stm->rowCount() == 0)
{
    echo 'No products';
    exit();
}

$rows = $stm->fetchAll(PDO::FETCH_ASSOC);

echo json_encode($rows);

?>
