var acc = document.getElementsByClassName("accordion");
var i;

for (i = 0; i < acc.length; i++) {
	acc[i].onclick = function() {
		this.classList.toggle("active");
		var panel = this.nextElementSibling;
		if (panel.style.maxHeight) {
			panel.style.maxHeight = null;
		} else {
			panel.style.maxHeight = panel.scrollHeight + "px";
		}
	}
};


var _targetdiv = null;
function showdiv(id) {
    if(_targetdiv)
        _targetdiv.style.display = 'none';
    _targetdiv = document.getElementById('termsofservice');
    _targetdiv.style.display = 'none';
    _targetdiv = document.getElementById(id);
    _targetdiv.style.display = 'block';
};