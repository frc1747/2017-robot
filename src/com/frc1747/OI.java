package com.frc1747;



	public class OI {
		
		private LogitechController controller;
		public OI(){
			controller = new LogitechController(0);
			//controller.getButtonX().whileHeld(new Drive());
		}
		
		public LogitechController getController(){
			return controller;
		}
	}
		

