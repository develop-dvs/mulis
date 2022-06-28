Name "Mulis ShareLink"
Caption "Mulis ShareLink"
Icon "Mulis_settings.ico"
OutFile "Mulis\Создать_сетевой_диск_и_ярлык.exe"
 
VIAddVersionKey "ProductName" "Mulis ShareLink"
VIAddVersionKey "Comments" "Mulis ShareLink"
VIAddVersionKey "CompanyName" "Divasoft, inc."
VIAddVersionKey "LegalTrademarks" "Mulis ShareLink a trademark of Divasoft, inc."
VIAddVersionKey "LegalCopyright" "Divasoft, inc."
VIAddVersionKey "FileDescription" "Mulis ShareLink"
VIAddVersionKey "FileVersion" "1.0.0"
VIProductVersion "0.0.0.2"

RequestExecutionLevel user
AutoCloseWindow true
ShowInstDetails show
Var SLASH
Section ""
RMDir /r "$PROFILE\.mulis"
CreateDirectory "$SMPROGRAMS\Mulis"
StrCpy $SLASH $EXEDIR 2
StrCmp $SLASH "\\" MountDisk CreateLink

MountDisk:
ReadEnvStr $R0 COMSPEC
;nsExec::Exec "$R0 /C net use x: \delete \no"
nsExec::Exec "$R0 /C net use x: $EXEDIR"
SetOutPath "x:\bin"
CreateShortCut "$DESKTOP\Mulis.lnk" "x:\bin\Mulis.exe" "" "x:\bin\Mulis.exe" 0
CreateShortCut "$SMPROGRAMS\Mulis\Mulis.lnk" "x:\bin\Mulis.exe" "" "x:\bin\Mulis.exe" 0
CreateShortCut "$SMPROGRAMS\Mulis\Удаленное подключение.lnk" "x:\bin\Support.exe" "" "x:\bin\Support.exe" 0
SetOutPath "x:\"
CreateShortCut "$SMPROGRAMS\Mulis\Обслуживание.lnk" "x:\Обслуживание.cmd" "" "x:\Обслуживание.cmd" 0
Return

CreateLink:
SetOutPath "$EXEDIR\bin"
CreateShortCut "$DESKTOP\Mulis.lnk" "$EXEDIR\bin\Mulis.exe" "" "$EXEDIR\bin\Mulis.exe" 0
CreateShortCut "$SMPROGRAMS\Mulis\Mulis.lnk" "$EXEDIR\bin\Mulis.exe" "" "$EXEDIR\bin\Mulis.exe" 0
CreateShortCut "$SMPROGRAMS\Mulis\Удаленное подключение.lnk" "$EXEDIR\bin\Support.exe" "" "$EXEDIR\bin\Support.exe" 0
SetOutPath "$EXEDIR"
CreateShortCut "$SMPROGRAMS\Mulis\Обслуживание.lnk" "$EXEDIR\Обслуживание.cmd" "" "$EXEDIR\Обслуживание.cmd" 0
SectionEnd
