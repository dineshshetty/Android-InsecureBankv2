console.log("Bypassing Root Check on InsecureBankv2");
 
Java.perform(function () {

var MainActivity = Java.use("com.android.insecurebankv2.PostLogin");


MainActivity.doesSuperuserApkExist.implementation = function(v) {
	console.log(v);
	console.log("MainActivity.doesSuperuserApkExist called");
	return false;
}

MainActivity.doesSUexist.implementation = function() {
	console.log("MainActivity.doesSUexist called");
	return false;
};

});
console.log("Done!");
