<?php
	

	if ($_SERVER["REQUEST_METHOD"] == "POST") {
   
	$type=$_REQUEST['type'];
	

		$uid = $_POST["uid"];
		$encoded_string = $_POST["encoded_string"];
		$image_name = $_POST["image_name"];
		//$idatetime = date("Y-m-d",time());
		$idatetime = "18-10-2016 00:22:22";
		
	
			$conn = new mysqli("mysql.hostinger.in", "u869259413_akx", "myfaceindia", "u869259413_mfi");
			// Check connection
			if ($conn->connect_error) {
	  			  die("Connection failed: " . $conn->connect_error);
			} 
			
			
			$sql = "INSERT INTO postimage(uid,imgpath,idatetime) 
					VALUES ('$uid' , '$encoded_string', '$idatetime')";

			if ($conn->query($sql) === TRUE) 
			{
				
				echo "success";
			} 
			else 
			{
				echo "Error: " . $sql . "<br>" . $conn->error;
			}
		
	
		
			$conn->close();
	
}
?>