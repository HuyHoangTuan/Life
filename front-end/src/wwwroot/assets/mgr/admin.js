var lastButton;
function dropdown(element) {
  var dropdowns;
  // if(element=document.getElementById("admin-account-setting")){
  //   dropdowns = document.getElementById("admin-account-setting-content");
  //   if (lastButton == element) {
  //     dropdowns.classList.toggle("show");
  //   } else {
  //     dropdowns.classList.add("show");
  //   }
  // }
  // if(element=document.getElementById("album-setting")){
  //   dropdowns=document.getElementById("album-setting-content");
  //   if (lastButton == element) {
  //     dropdowns.classList.toggle("show");
  //   } else {
  //     dropdowns.classList.add("show");
  //   }
  // }
  // dropdowns=document.getElementById(`${element.id}-content`);
  if(element=document.getElementById("admin-account-setting")) dropdowns=document.getElementById("admin-account-setting-content");
  // else if(element.classList.contains("album-setting")) dropdowns=document.getElementById("album-setting-content");
  // console.log()
  if (lastButton == element) {
    dropdowns.classList.toggle("show");
  } else {
    dropdowns.classList.add("show");
  }
  lastButton = element;
  var a = element.getBoundingClientRect();
    dropdowns.style.position="fixed";
    dropdowns.style.top=a.bottom / 2 + "px";
    dropdowns.style.right= window.innerWidth - a.right + "px";
    console.log(a.left);
    console.log(a.bottom);
  }  
  var prevButton;
  function deleteConfirm(element){
    var popup;
    popup=document.getElementById("delete-confirm-content");
    if(prevButton == element){
      popup.classList.toggle("show");
    } else{
      popup.classList.add("show");
    }
    prevButton=element;
  }
  function cancel(element){
    var popup;
    popup=document.getElementById("delete-confirm-content");
    popup.classList.toggle("show");
  }
  