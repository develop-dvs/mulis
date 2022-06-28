"c:\Program Files (x86)\NSIS3\makensis.exe" launcher.nsi
"c:\Program Files (x86)\NSIS3\makensis.exe" linkcreate.nsi
"c:\Program Files (x86)\NSIS3\makensis.exe" teamviwer.nsi
"c:\Program Files (x86)\NSIS3\makensis.exe" Mulis_withJRE.nsi
"c:\Program Files (x86)\NSIS3\makensis.exe" Mulis_withoutJRE.nsi
del build-0002-light.zip
7z.exe a -r build-0002-light.zip Mulis\*.*
pause

copy build-0002-light.zip "D:\Dropbox\pub\dl\mulis\0002\build-0002-light.zip"
copy build-0.0.0.2-light.exe "D:\Dropbox\pub\dl\mulis\0002\build-0002-light.exe"
copy build-0.0.0.2-full.exe "D:\Dropbox\pub\dl\mulis\0002\build-0002-full.exe"
pause

notepad "D:\Dropbox\pub\dl\mulis\version.txt"