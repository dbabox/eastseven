var Appcheck = Appcheck ? Appcheck :{};
var formCheck;
var thirdSd = "";
var box2;
var applicationVersion;
Appcheck = new Class({

	options:{
    	
	},
	initialize: function(){        
		this.drawUserBox();
		this.drawGrid();
		this.drawUserBox2();
	},
	drawUserBox : function() {
		this.userBox = new LightFace( {
			draggable : true,
			initDraw : false,
			onClose : function() {
				var div = document.getElement('div[class=fc-tbx]');
				if ($chk(div)) {
					div.dispose();
				}
			},
			buttons : [ {
				title : '保 存',
				event : function() {
					this.appForm.getElement('button').click();
				}.bind(this),
				color : 'blue'
			}, {
				title : '关 闭',
				event : function() {
					this.close();
				}
			} ]
		});
	},
	drawUserBox2 : function() {
		this.userBox2 = new LightFace( {
			draggable : true,
			initDraw : false,
			onClose : function() {
				var div = document.getElement('div[class=fc-tbx]');
				if ($chk(div)) {
					div.dispose();
				}
			},
			buttons : [ {
				title : '关 闭',
				event : function() {
					this.close();
				}
			} ]
		});
	},
	drawGrid : function() {
		this.grid = new JIM.UI.Grid('tableDiv', {
			url : ctx + '/html/requistion/?m=index',
			multipleSelection : false,
			buttons : [ 
			{
				name : '审核',
				icon : ctx + '/admin/images/icon_9.png',
				handler : function() {
					var selectIds = this.grid.selectIds;
					if (!$chk(selectIds) || selectIds.length == 0) {
						new LightFace.MessageBox().error('请先选择列表中的信息');
						return;
					}
					this.userBox.options.title = '审核信息';
					this.userBox.options.titleImage = ctx + '/admin/images/icon_9.png';
					this.userBox.options.content = $('requistionDiv').get('html');
					this.userBox.addEvent('open', this.openEditApp.bind(this));
					this.userBox.options.width = 740;
					this.userBox.options.height = 360;
					this.userBox.open();
					this.userBox.removeEvents('open');
				}.bind(this)
			},			
			{
				name : '查看应用版本',
				icon : ctx + '/admin/images/page_white_edit.png',
				handler : function() {
					var selectIds = this.grid.selectIds;
					if (!$chk(selectIds) || selectIds.length == 0) {
						new LightFace.MessageBox().error('请先选择列表中的信息');
						return;
					}
					this.userBox2.options.title = '应用版本详情';
					this.userBox2.options.titleImage = ctx + '/admin/images/page_white_edit.png';
					this.userBox2.options.content = $('appVerInfoDiv').get('html');
					this.userBox2.addEvent('open', this.openViewAppVer.bind(this));
					this.userBox2.options.width = 800;
					this.userBox2.options.height = 360;
					this.userBox2.open();
					this.userBox2.removeEvents('open');
					this.userBox2.addEvent('close', this.closeWin.bind(this));
				}.bind(this)
			}
			],
        	columnModel : [{dataName : 'id', identity : true},{title : '应用名称', dataName : 'appName',order : false}, {title : '类型', dataName : 'type'},
        	               {title : '版本号', dataName : 'versionNo',order : false},  {title : '申请理由', dataName : 'reason'}, {title : '提交时间', dataName : 'submitDate'},
      	            	  {title : '状态', dataName : 'status'}],
			searchButton : true,
        	searchBar : {filters : [{title : '应用名：', name : 'appName', type : 'text'}
        	]},
			headerText : '应用审核',
			headerImage : ctx + '/images/user_icon_32.png'
		});
	},
	openEditApp : function() {
		thirdSd = "";
		var selectIds = this.grid.selectIds;
		this.appForm = this.userBox.messageBox.getElement('form');
		var box = this.userBox.messageBox;
		var opinion = this.userBox.messageBox.getElement('input[name=opinion]');
		this.userBox.messageBox.getElement('form').set('action', ctx + '/html/requistion/?m=updatePublish');
		this.userBox.messageBox.getElement('select[name=statusOriginal]').addEvent('change', function(){
			if(this.get('value') == 3){
				formCheck.dispose(opinion);
				if (opinion.get('value') == ''){
					opinion.set('value','同意');
				}
				opinion.set('class',"inputtext validate['%chckMaxLength']");
				formCheck.register(opinion);
				var thirdInputs = box.getElements('select[name="sdids"]');
				thirdInputs.each(function(e){
					formCheck.register(e);
				});
			}else{
				formCheck.dispose(opinion);
				if (opinion.get('value') == '同意'){
					opinion.set('value','');
				}
				opinion.set('class',"inputtext validate['required','%chckMaxLength']");
				formCheck.register(opinion);
				var thirdInputs = box.getElements('select[name="sdids"]');
				thirdInputs.each(function(e){
					formCheck.dispose(e);
				});
			}
		});
		new Request.JSON( {
			url : ctx + '/html/requistion/?m=index&id=' + selectIds[0],
			onSuccess : function(data) {
				if (data.success) {
					var appStatus = data.result[0]['appStatus'];
					var inputs = this.userBox.messageBox.getElements('input,select');
					this.appForm = this.userBox.messageBox.getElement('form');
					var files = this.userBox.messageBox.getElement('table[name=files]');
					var box = this.userBox.messageBox;
					if (appStatus != 0){
						this.userBox.messageBox.getElement('[id=typeTktd]').setStyle('display','none');
						this.userBox.messageBox.getElement('[id=tkVersiontd]').setStyle('display','none');
						this.userBox.messageBox.getElement('[id=kekVersiontd]').setStyle('display','none');
						this.userBox.messageBox.getElement('[id=typeKektd]').setStyle('display','none');
						this.userBox.messageBox.getElement('[id=tkVendortd]').setStyle('display','none');
						this.userBox.messageBox.getElement('[id=kekVendortd]').setStyle('display','none');
					}
					$each(inputs, function(input, i) {
						input.set('value', data.result[0][input.get('name')]);
					});
					new Request.JSON(
							{
								url : ctx
+ "/html/commons/?m=exportEnum&enumName=com.justinmobile.tsm.cms2ac.security.scp02.EncryptorVendor&exportMethodName=export",
								onSuccess : function(json) {
									if (json.success) {
										transConstant = json.message;
										var jsonHash = new Hash(transConstant);
										var option = new Element('option').set('value','').set('text','请选择...');
										option.inject(box.getElement("[id='tkVendor']"));
										option = new Element('option').set('value','').set('text','请选择...');
										option.inject(box.getElement("[id='kekVendor']"));
										jsonHash.each(function(value, key) {
											option = new Element('option').set('value',value.value).set('text',value.name);
											option.inject(box.getElement("[id='tkVendor']"));
											option = new Element('option').set('value',value.value).set('text',value.name);
											option.inject(box.getElement("[id='kekVendor']"));
										});
									}
								}
							}).get();
					new Request.JSON( {
						url : ctx + '/html/loadFile/?m=loadByIds&ids=' + data.result[0]['loadFileIds'],
						onSuccess : function(result) {
							if (result.success) {
								new Request.JSON({
									url : ctx + "/html/securityDomain/?m=index",
									onSuccess : function(json) {
										if (json.success) {
											json.result.forEach(function(e, index) {
												thirdSd += "<option value='"+e.id+"'>"+e.sdName+"</option>";
											});
											var html = "<tr><td align=\"center\">加载文件名</td><td  align=\"center\">" +
													"所属安全域类型</td><td  align=\"center\">所属安全域名称</td></tr>";
											result.result.forEach(function(e, index) {
												//var name=	DataLength(e.name,15);
												//alert(data.result[0]['type']);
												html += "<tr><td  align=\"left\">"+e.name+"</td><td  align=\"left\">"+e.sdModel+"</td><td  align=\"center\">";
												if ((e.sd_id != null && e.sd_id != "") || e.sdModelOriginal != '2' || data.result[0]['typeOriginal'] == '12'){
													//alert(e.sd_id);
													html += "<input name='sdids2' id='"+e.id+"' class=\"validate['%notThirdSdCheck']\" readonly='readonly' " +
															"value='"+(e.sd_sdName == undefined? '无安全域':e.sd_sdName)+"' /></td>";
												}else{
													html += "<select name='sdids' id='"+e.id+"' class=\"validate['required']\">" +
																"<option value=''>请选择...</option>"+thirdSd+"</select></td></tr>";
												}
											});
											files.set('html',html);
											this.addValidate();
										}
									}.bind(this)
								}).get({'search_EQI_status' : 2, 'search_EQI_model' : 2, 'page_pageSize' : 10000000});
							} else {
								this.addValidate();
								new LightFace.MessageBox().error("没有对应的加载文件");
							}
						}.bind(this)
					}).get();
				} else {
					new LightFace.MessageBox().error(data.message);
				}
			}.bind(this)
		}).get();
	},
	closeWin : function() {
//		location.reload();
	},
	openViewAppVer : function() {
		thirdSd = "";
		var selectIds = this.grid.selectIds;
		this.appForm = this.userBox2.messageBox.getElement('form');
		box2 = this.userBox2.messageBox;
		new Request.JSON( {
			url : ctx + '/html/requistion/?m=index&id=' + selectIds[0],
			onSuccess : function(data) {
				if (data.success) {
					data.result.forEach(function(e, index) {
						var page = new Application.Page({
							applicationVersionId : e.originalId
						});
						page.init();
					});
				} else {
					new LightFace.MessageBox().error(data.message);
				}
			}.bind(this)
		}).get();
	},
	openTestFile : function() {
		var selectIds = this.grid.selectIds;
		this.appForm = this.userBox2.messageBox.getElement('form');
		box2 = this.userBox2.messageBox;
		new Request.JSON( {
			url : ctx + '/html/requistion/?m=index&id=' + selectIds[0],
			onSuccess : function(data) {
				if (data.success) {
					data.result.forEach(function(e, index) {
						new Request.JSON({
							url : ctx + "/html/appVer/?m=index",
							data : {
								async : false,
								search_EQL_id : e.originalId
							},
							onSuccess : function(data2) {
								if (data2.success) {
									data2.result.forEach(function(e, index) {
									//	alert(e.id);
										new Request.JSON({
											url : ctx + "/html/testfile/?m=index",
											data : {
												async : false,
												search_ALIAS_appVerL_EQL_id : e.id
											},
											onSuccess : app.getTestAppFile.bind(this)
										}).get();
									});
								} else {
									new LightFace.MessageBox().error(data.message);
								}
							}.bind(this)
						}).get();
					});
				} else {
					new LightFace.MessageBox().error(data.message);
				}
			}.bind(this)
		}).get();
	},
	getTestAppFile : function(json) {
		if (json.success) {
			json.result.forEach(function(e, index) {
				new Element('dd').set('html','<p class="regtextleft">'+e.originalName+
						'</p>&nbsp;&nbsp;<a class="b"  style="float : none;"  href="' + ctx + '/html/testfile/?m=downFile&tfId=' + e.id + '"><span>下载</span></a>')
				.inject(box2.getElement("[id='testFile']"));
			});
			if (json.result.length == 0){
				new Element('dd').set('html',"没有测试文件").inject(box2.getElement("[id='testFile']"));
			}
		} else {
			alert("获取测试文件失败" + json.message);
		}
	},
	addValidate : function() {
		formCheck = new FormCheck(this.appForm, {
			submit : false,
			zIndex : this.userBox.options.zIndex,
			display : {
				showErrors : 0,
				indicateErrors : 1,
				scrollToFirst : false
			},
			onValidateSuccess : function() {//校验通过执行load()
				var sdidStr = "";
				this.appForm.getElements('select[name=\'sdids\']').forEach(function(e, index) {
					sdidStr += (e.id + ";"+e.value+",");
				});
				this.userBox.messageBox.getElement('input[name=sdidStr]').set('value',sdidStr);
				this.submitForm();
			}.bind(this)
		});
	},
	submitForm : function() {
		new Request.JSON( {
			url : this.appForm.get('action'),
			onSuccess : function(data) {
				if (data.success) {
					new LightFace.MessageBox( {
						onClose : function() {
							this.userBox.close();
							this.grid.load();
						}.bind(this)
					}).info(data.message);
				} else {
					new LightFace.MessageBox().error(data.message);
				}
			}.bind(this)
		}).post(this.appForm.toQueryString());
	}
});
function DataLength(fData,expectLength)   
{   
    var intLength=0;   
    for (var i=0;i<fData.length;i++)   
    {   
        if ((fData.charCodeAt(i) < 0) || (fData.charCodeAt(i) > 255))   {
            intLength=intLength+2;   
        }else if ((fData.charCodeAt(i) > 48) && (fData.charCodeAt(i) < 58)){
        	intLength=intLength+1.3;  
        }
        else  {
            intLength=intLength+1;       
        }
        if (expectLength<intLength){
        	return fData.substring(0,i)+'...';
        }
    }
    return fData;
}
function notThirdSdCheck(el){ 
	if (el.value == '无安全域'){
        el.errors.push("非公共第三方安全域不存在，不能提交审核");
        return false;
	}
}