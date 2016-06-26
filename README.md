# QRParserLib

This lib intend to help developers to parse the qr text returned from any qr reader to an elegant form which you can display in a textview 

and the ability to return a well formated object which you can use to get desired field.

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-QRParserLib-green.svg?style=true)](https://android-arsenal.com/details/1/3785)

# Features supported by Lib

- gives you the ability to change the QR reader replace it with your choice and the parser will be the same
- Keep history to all QR codes scanned before
- parse QR codes to a well formatted object which contain a raw value you can use to display it into textview or use it to get specific   values
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

###TODO

- support gradle 

###License

    The MIT License (MIT)

    Copyright (c) 2016 Ahmed basyouni

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.



