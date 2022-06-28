"c:\Program Files (x86)\NSIS13\makensis.exe" launcher.nsi
"c:\Program Files (x86)\NSIS13\makensis.exe" linkcreate.nsi
"c:\Program Files (x86)\NSIS13\makensis.exe" teamviwer.nsi
"c:\Program Files (x86)\NSIS13\makensis.exe" Mulis_withJRE.nsi
"c:\Program Files (x86)\NSIS13\makensis.exe" Mulis_withoutJRE.nsi
del mulis-0.0.0.1.zip
7z.exe a -r mulis-0.0.0.1.zip Mulis\*.*
pause