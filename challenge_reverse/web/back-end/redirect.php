<?php

// init session
session_start();
$session_version = 'version-' . strval($app_version);

if(!isset($_SESSION[$session_version]))
{
    $_SESSION[$session_version] = array();
}

// limit number of requests in a given time
if(!isset($_SESSION['requests_times']))
{
    $_SESSION['requests_times'] = array(time());
    $_SESSION['banned']=0;
}
else
{
    $max_requests = 80; // max attempts
    $min_time = 60; // seconds
    $requests_number = count($_SESSION['requests_times']);

    if($requests_number > $max_requests) // raz
    {
        for($i = $max_requests; $i < $requests_number; $i++)
        {
            array_pop($_SESSION['requests_times']);
        }
    }
    else if($requests_number == $max_requests)
    {
        $oldest_request_time_index = $max_requests - 1;
        $oldest_request_time = $_SESSION['requests_times'][$oldest_request_time_index];
        array_pop($_SESSION['requests_times']);
        array_unshift($_SESSION['requests_times'], time());
        if($oldest_request_time > time() - $min_time)
        {
            echo 'Access denied'; // too many requests
            $_SESSION['banned']=1;
            exit();
        }else if ($_SESSION['banned']){
        $_SESSION['requests_times'] = array(time());
    }
    else
    {
        array_unshift($_SESSION['requests_times'], time());
    }
}

// if both page and request selected => bad request
if(isset($_GET['request']) && isset($_GET['page']))
{
    echo 'Bad request';
    exit();
}

function add_version_header()
{
    global $app_version;
    echo "<!-- Shopping Express v" . $app_version . " -->\n";
    if(floatval($app_version) >= 1.0)
    {
        echo "<!-- The updated site is now up ! -->\n";
    }
    echo "\n";
}

// if no page and no request selected => login by default
if(!isset($_GET['request']) && !isset($_GET['page']))
{
    add_version_header();
    require_once($dir_path . "front-end/login.html");
    exit();
}

// if page selected => page selector (html front-end pages)
if(isset($_GET['page']))
{
    switch($_GET['page'])
    {
        case "login":
            add_version_header();
            require_once($dir_path . "front-end/login.html");
            break;
        case "home":
            add_version_header();
            if(!isset($_SESSION[$session_version]['signed_in']) || !$_SESSION[$session_version]['signed_in'])
            {
                require_once($dir_path . "front-end/login.html");
                exit();
            }
            require_once($dir_path . "front-end/home.html");
            break;
        default:
            echo "Bad request";
    }
    exit();
}

// if request selected => request selector (php back-end api)
switch($_GET['request'])
{
    case "sign_in":
        require_once($dir_path . "back-end/db_connect.php");
        require_once($dir_path . "back-end/login_handler.php");
        break;
    case "sign_out":
        require_once($dir_path . "back-end/log_out.php");
        break;
    case "get_products":
        require_once($dir_path . "back-end/check_connected.php");
        require_once($dir_path . "back-end/db_connect.php");
        require_once($dir_path . "back-end/get_products.php");
        break;
    case "buy_product":
        require_once($dir_path . "back-end/check_connected.php");
        require_once($dir_path . "back-end/db_connect.php");
        require_once($dir_path . "back-end/buy_product.php");
        break;
    case "get_profile":
        require_once($dir_path . "back-end/check_connected.php");
        require_once($dir_path . "back-end/db_connect.php");
        require_once($dir_path . "back-end/get_profile.php");
        break;
    case "get_authentication_code":
        require_once($dir_path . "back-end/get_authentication_code.php");
        break;
    default:
        echo "Bad request";
}

?>
