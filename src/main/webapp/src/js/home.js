function setCookie(cname, cvalue) {
  const d = new Date();
  exdays=1;
//  d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));//days
d.setTime(d.getTime() + (60*60*1000));
  let expires = "expires="+d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires ;
}
function getCookie(cname) {
  let name = cname + "=";
  let ca = document.cookie.split(';');
  for(let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

function checkCookie() {
  let user = getCookie("username");
  if (user != "" && user!=null) {
    setCookie("username",user);
  } else {
  document.location = "login.html";
  }
}

checkCookie();