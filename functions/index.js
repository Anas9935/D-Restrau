/*const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp();
exports.addMessage=functions.https.onRequest(async(req,res)=>{
	const original=req.query.text;
	const snapshot =await admin.database().ref('/messages').push({original:original});
	res.redirect(303,snapshot.ref.toString());
		});
exports.makeUppercase = functions.database.ref('/messages/{pushId}/original')
    .onCreate((snapshot, context) => {
      // Grab the current value of what was written to the Realtime Database.
      const original = snapshot.val();
      console.log('Uppercasing', context.params.pushId, original);
      const uppercase = original.toUpperCase();
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      return snapshot.ref.parent.child('uppercase').set(uppercase);
    });
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
*/
const admin = require('firebase-admin');
admin.initializeApp();
//import * as functions from 'firebase-functions'
const functions=require('firebase-functions');
exports.onMessageCreate=functions.database
.ref(`/messages/{restId}/{messageId}`)
.onCreate((snapshot,context)=>{
const restId=context.params.restId;
const messageId=context.params.messageId;
console.log(`New Message ${messageId} in the resttrau ${restId}`);

const messageData=snapshot.val();
const message=addText(messageData.message);
return snapshot.ref.update({message: message});
})
//module.exports =onMessageCreate;
function addText(text){
	return text.replace(/\banas\b/g,'admin');
}







