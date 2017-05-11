//动态改变页面宽度
document.documentElement.style.fontSize=document.documentElement.clientWidth/6.4+"px";
window.addEventListener("resize",fn,false);
function fn(){
    document.documentElement.style.fontSize=document.documentElement.clientWidth/6.4+"px";
}
if(window.location.host=='www.ditingai.com'||window.location.host=='ditingai.com'){
    document.title = '谛听机器人开放平台';
}else if(window.location.host=='antusheng.cn'){
    document.title = '谛听客服机器人'
}