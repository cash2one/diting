/**
 * Created by Administrator on 2017/4/25/025.
 */
window.onload=function(){
	var event = {
		"topUl":$("#topUl"),
					"topLi0":$(".topLi0"),
					"navLi_zsk":$("#navLi_zsk"),
					"navLi_ck":$("#navLi_ck"),
                    "ckLi":$(".ckLi"),
					"flag0":true,
					"flag1":true,
					"init":function(){
						this.topUl.mouseenter(function(){
							this.navOut()
						}.bind(this))
						this.topUl.mouseleave(function(){
							this.navIn()
						}.bind(this))

						this.navLi_zsk.click(function(){
							if(this.flag0){
								this.navLi_zsk.css({
									"height":"180px",
									"transition": "height 0.5s",
									"-moz-transition": "height 0.5s",
									"-webkit-transition": "height 0.5s",
									"-o-transition": "height 0.5s"
								})
								this.flag0=!this.flag0
							}else{
								this.navLi_zsk.css({
									"height":"60px",
									"transition": "height 0.5s",
									"-moz-transition": "height 0.5s",
									"-webkit-transition": "height 0.5s",
									"-o-transition": "height 0.5s"
								})
								this.flag0=!this.flag0
							}
						}.bind(this))
						this.navLi_ck.click(function(){
							if(this.flag0){
								this.navLi_ck.css({
									"height":"380px",
									"transition": "height 1s",
									"-moz-transition": "height 1s",
									"-webkit-transition": "height 1s",
									"-o-transition": "height 1s"
								})
								this.flag0=!this.flag0
							}else{
								this.navLi_ck.css({
									"height":"60px",
									"transition": "height 1s",
									"-moz-transition": "height 1s",
									"-webkit-transition": "height 1s",
									"-o-transition": "height 1s"
								})
								this.flag0=!this.flag0
							}
						}.bind(this))
                        this.ckLi.click(function(event){
							event.stopPropagation();
							$(this).css("background","#999").siblings().css("background","rgb(50,50,50)")
						})
						this.topLi0.mouseenter(function(){
							$(this).css("background","#CCC").parent().siblings().children(".topLi0").css("background","white")
						})
					},
					"navOut":function(){
						this.topUl.css({
							"height":"156px",
							"transition": "height 0.5s",
							"-moz-transition": "height 0.5s",
							"-webkit-transition": "height 0.5s",
							"-o-transition": "height 0.5s"
						})
					},
					"navIn":function(){
						this.topUl.css({
							"height":"42px",
							"transition": "height 0.5s",
							"-moz-transition": "height 0.5s",
							"-webkit-transition": "height 0.5s",
							"-o-transition": "height 0.5s"

						})
						this.topUl.children().css("background","white")
					}
				}
				event.init();
			}
