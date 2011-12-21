var UnLost = UnLost ? UnLost : {};

UnLost = new Class({
	options : {},
	nowId : '',
	transConstant : '',
	aid : '',
	initialize : function() {
     	this.getConstants();
	},
	getConstants : function() {
		new Request.JSON({
			url : ctx + "/html/localtransaction/?m=exportConstant",
			onSuccess : function(json) {
				if (json.success) {
					this.transConstant = json.message;
				}
			}.bind(this)
		}).get();
	},
	unLost : function(ccid,gird){
		var obj = this;
		new Request.JSON({
			url : ctx + '/html/cardSd/?m=getSdByCci&ccid=' + ccid,
			onSuccess : function(data){
				if(data.success){
					if(data.message.card == 1){
						var cardNo = data.message.cardNo;
						var nowNo = new JIM.CardDriver({
							ctl : cardDriver,
							operations : []
						}).readCardNo();
						if(cardNo != nowNo){
							new LightFace.MessageBox().error("当前终端不是解挂操作的终端");
							return;
						}else{
							new JIM.CardDriver({
								ctl : cardDriver,
								showMsg : false,
								operations : [ {
									aid : '', 
									operation : obj.transConstant.UNLOCK_CARD
								} ],
								onSuccess : function(response){
									    new Request.JSON({
										url : ctx + '/html/customerCard/?m=cancelLost',
										onSuccess : function(resp){
											if(resp.success){
												new LightFace.MessageBox({
													onClose : function(){
														gird.load();
													}
												}).info('解除挂失成功');
											}else{
												new LightFace.MessageBox().error(resp.message);
											}
										}
									}).post({
										'ccid' : ccid
									}); 
								},
								onFailure : function(){
								    new LightFace.MessageBox().error("无法解锁卡片，不能解除挂失状态");
									return;
								}
							}).exec();
						}
					}else{
						new Request.JSON({
							url : ctx + '/html/customerCard/?m=cancelLost',
							onSuccess : function(resp){
								if(resp.success){
									new LightFace.MessageBox({
										onClose : function(){
											gird.load();
										}
									}).info('解除挂失成功');
								}else{
									new LightFace.MessageBox().error(resp.message);
								}
							}
						}).post({
							'ccid' : ccid,
							'delOpt' : 1
						}); 			
					}			
				}else{
					new LightFace.MessageBox().error(data.message);
				}
			}
		}).get();
	}
});