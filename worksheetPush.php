<?php

	//建立連線
	$con=mysqli_connect("localhost","ezgo","ezgo","ezgo")or die("Error " . mysqli_error($con));
	//設定字碼集
	mysqli_query($con,"set names utf8"); 
	
	if (mysqli_connect_errno($con))
	{
	   echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}
	
	$sql = "SELECT * FROM worksheet";
	
	$res = mysqli_query($con,$sql)or die("Error in Selecting " . mysqli_error($con));; 
	$result = array();
	
	while($row = mysqli_fetch_array($res)){
		array_push($result,array('No'=>$row[0]));
	}
	
	echo json_encode(array("result"=>$result),JSON_UNESCAPED_UNICODE);
	
	mysqli_close($con);
?>



