/**
 *
 */
			var ok1=false;
            var ok2=false;
            var ok3=false;
            var ok4=false;
            var ok5=false;
			var msgBox = "#messageBox";
			var msgBoxError = "#loginError";
            var frontPath = $("#frontPath").text();
            var mobile = $("input[name='mobile']").val();
			$("input[name='mobile']").blur(function(){
				mobile = $("input[name='mobile']").val();
				if(mobile.search(/^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$/)==-1){
					$(msgBoxError).html("请输入正确的手机号！");
					domIsShowMsg(true, true);
				}else{
					$.ajax({
						type: "POST",
						url: frontPath+"/register/validatePhone",
					    data: {'mobile':mobile},
						success: function(msg){
						   if(!msg){
								$(msgBoxError).html("该手机号已被注册！");
								domIsShowMsg(true, true);

							   $("a").attr("disabled", true);
						   }else{
								$(msgBoxError).html("");
								domIsShowMsg(false, false);
							   Count_down.time();
							   ok1=true;
						   }
						 },
						 error:function(msg){
							$(msgBoxError).html("该手机号已被注册 ！");
							domIsShowMsg(true, true);
						 }

					});
				}
			});



			var Count_down={
				    second:60,
				    $btn:$('#OK'),
				    time:function(){
				        var _this=this;
				        this.$btn.on('click',function(){
				        	if(_this.$btn.prop('disabled'))return;
				        	$.ajax({
								type:"POST",
								url:frontPath+"/register/getVerificationCode",
								data:{'mobile':mobile},
								success:function(msg){
									if(msg!=null){
										ok2=true
									}
								}

							});
				        	_this.Interval();
				            _this.timer=window.setInterval(function(){
				            	_this.Interval();
				            },1000);
				        })
				    },
				    Interval:function(){
				    	this.$btn.prop('disabled',true).val('('+(--this.second)+'s)'+'后重新发送');
		                if(this.second==0){
		                    clearInterval(this.timer);
		                    this.$btn.prop('disabled',false).val('获取验证码');
		                    this.second=60;
		                }
				    }
				}

//			$(function(){

//			});

//			console.log(Count_down.$btn);

//			var ms;
//			$("#btn").click(function(){
//				Count_down.time();
//				$.ajax({
//					type:"POST",
//					url:frontPath+"/register/getVerificationCode",
//					data:{'mobile':mobile},
//					success:function(msg){
//						if(msg!=null){
//							ms = msg;
//							ok2=true
//						}
//					}
//
//				});
//			});

			$("#yzma").blur(function(){
				var yanzhengma = $("#yzma").val();
				if(yanzhengma!=null && yanzhengma!=""){
					$.ajax({
						type: "POST",
						url: frontPath+"/register/validateYZM",
					    data: {'yzma':yanzhengma},
						success: function(msg){
							if(msg==1){
								$(msgBoxError).html("");
								domIsShowMsg(false, false);
								ok3=true;
							}else{
								$(msgBoxError).html("验证码输入有误！");
								domIsShowMsg(true, true);
							}
						 }

					});
				}else{
					$(msgBoxError).html("请输入验证码！");
					domIsShowMsg(true, true);
				}

			});

			//验证密码
            $('input[name="password"]').focus(function(){
				$(msgBoxError).html('*请输入6-20位的密码！');
				domIsShowMsg(true, true);
            }).blur(function(){
                if($(this).val().length >= 6 && $(this).val().length <=20 && $(this).val()!=''){
    				$(msgBoxError).html("");
					domIsShowMsg(false, false);
                    ok5=true;
                }else{
    				$(msgBoxError).html('*密码应该为6-20位之间');
    				domIsShowMsg(true, true);
                }
            });

			$("input[name='confirm_password']").blur(function(){
				var password = $("#password").val();
				var repassword = $("#repassword").val();
				if(password==repassword){
					ok4=true;
				}else{
    				$(msgBoxError).html("两次密码输入不一样！");
    				domIsShowMsg(true, true);
				}
				if(ok1 && ok2 &&ok3 && ok4 && ok5){
					$("#btRegister").css("background-color","red");
				}
			});




			//dom显示隐藏
			function domIsShow(dom, isShow){
				if(isShow == null || isShow == undefined){
					isShow = true;
				}
				if(isShow){
					$(dom).show();
					$(dom).css("display","block");
				}else{
					$(dom).hide();
					$(dom).css("display","none");
					if($(dom).hasClass("hide")){
						$(dom).removeClass("hide");
					}
				}
			}

			function domIsShowMsg(isShow, hasMsg){
				if(isShow && hasMsg){
					isShow = true;
				}else{
					isShow = false;
				}
				if(isShow){
					domIsShow($(msgBox), true);
					domIsShow($(msgBoxError), true);
				}else{
					domIsShow($(msgBox), false);
					domIsShow($(msgBoxError), false);
				}
			}

			function validateRegForm(){
				if(ok1 == false){
					$(msgBoxError).html("手机号码有误");
					domIsShowMsg(true, true);
					return false;
				}

				if(ok2 == false){
					$(msgBoxError).html("验证码有误");
					domIsShowMsg(true, true);
					//return false;
				}
				if(ok3 == false){
					$(msgBoxError).html("验证码有误");
					domIsShowMsg(true, true);
					return false;
				}
				if(ok4 == false){
					$(msgBoxError).html("两次输入登录密码不一致");
					domIsShowMsg(true, true);
					return false;

				}
				if(ok5 == false){
					$(msgBoxError).html("登录密码有误");
					domIsShowMsg(true, true);
					return false;
				}

				if(ok1 && ok2 &&ok3 && ok4 && ok5){
					domIsShowMsg(false, false);
					$("#registerfm").submit();
				}
			}

			function onkeydownFun(){
				document.onkeydown = function (event) {
					e = event ? event : (window.event ? window.event : null);
					if (e.keyCode == 13) {
						validateRegForm();
					}
				}
			}
			$(function(){
				$("#btRegister").click(function(){
					validateRegForm();
				});
				onkeydownFun();
			});
