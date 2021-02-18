<?php

// if both page and request selected => bad request
if(isset($_REQUEST['request']) && isset($_GET['page']))
{
    echo 'Bad request';
    exit();
}

// if no page and no request selected => login by default
if(!isset($_REQUEST['request']) && !isset($_GET['page']))
{
    require_once("../front-end/login.html");
    exit();
}

// if page selected => page selector (html front-end pages)
if(isset($_GET['page']))
{
    switch($_GET['page'])
    {
        case "login":
            require_once("../front-end/login.html");
            break;
        case "home":
            require_once("../front-end/home.html");
            break;
        default:
            echo "Bad request";
    }
    exit();
}

// if request selected => request selector (php back-end api)
switch($_REQUEST['request'])
{
    case "sign_in":
        require_once("../back-end/db_connect.php");
        require_once("../back-end/login_handler.php");
        break;
    case "sign_out":
        require_once("../back-end/log_out.php");
        break;
    case "get_products":
    case "buy_product":
    case "get_profile":
        require_once("../back-end/check_connected.php");
        require_once("../back-end/db_connect.php");
        require_once("../back-end/" . $_REQUEST['request'] . ".php");
        break;
    default:
        echo "Bad request";
}

?>