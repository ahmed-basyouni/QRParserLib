# QRParserLib

This lib intend to help developers to parse the qr text returned from any qr reader to an elegant form which you can display in a textview 

and the ability to return a well formated object which you can use to get desired field.

# Features supported by Lib

- gives you the ability to change the QR reader replace it with your choice and the parser will be the same
- Keep history to all QR codes scanned before
- parse QR codes to a well formated object which contain a raw value you can use to display it into textview 
  or use it to get scecific values
- ability to handle all action associated to a QR code (i.e for contact info it support show on map , call , send mail and add to contacts)
- easy to use

# How to use 

Well if you only need to use the default view mode all you have to do is to call
CaptureActivity
and that's it the view would look like that before capture 

<p align="center">
  <img src="https://raw.githubusercontent.com/ahmed-basyouni/gitImages/master/Screenshot_2016-06-22-20-25-39-647.jpeg" width="350"/>
</p>

and after detection

<p align="center">
  <img src="https://raw.githubusercontent.com/ahmed-basyouni/gitImages/master/Screenshot_2016-06-22-20-34-49-427.jpeg" width="350"/>
</p>

if that view doesn't go well with your teast well you can make your own view and to do that please follow manual installation in wiki
