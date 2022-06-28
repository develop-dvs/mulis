Name "Mulis TeamViewer DL"
Caption "Mulis TeamViewer DL"
Icon "Mulis_settings.ico"
OutFile "Mulis\bin\Support.exe"
 
VIAddVersionKey "ProductName" "Mulis TeamViewer DL"
VIAddVersionKey "Comments" "Mulis TeamViewer DL"
VIAddVersionKey "CompanyName" "Divasoft, inc."
VIAddVersionKey "LegalTrademarks" "TeamViewer DL a trademark of Divasoft, inc."
VIAddVersionKey "LegalCopyright" "Divasoft, inc."
VIAddVersionKey "FileDescription" "Mulis TeamViewer DL"
VIAddVersionKey "FileVersion" "1.0.0"
VIProductVersion "0.0.0.2"

!define TV_URL "http://www.teamviewer.com/download/TeamViewerQS_ru.exe"
;!define TV_URL "http://downloadus1.teamviewer.com/download/TeamViewerQS_ru.exe"
!define TV_FILE "TeamViewerQS_ru.exe"

RequestExecutionLevel user
AutoCloseWindow true
ShowInstDetails show

Section ""
;IfFileExists "x:\bin\${TV_FILE}" JustRun 
IfFileExists ${TV_FILE} JustRun StartDL
StartDL:
  ;SetOutPath "$EXEDIR"
	NSISdl::download /TIMEOUT=100000 ${TV_URL} "$EXEDIR/${TV_FILE}"
	Pop $R0 ;Get the return value
	  StrCmp $R0 "success" +3
	  MessageBox MB_OK "[http://www.teamviewer.com] Ошибка при загрузке: $R0"
	  Quit
  ;IfFileExists ${TV_FILE} JustRun ExitNow
JustRun:
;MessageBox MB_OK "RUN"
Delete "$SMPROGRAMS\Mulis\Удаленное подключение.lnk"
SetOutPath "$EXEDIR"
CreateShortCut "$SMPROGRAMS\Mulis\Удаленное подключение.lnk" "$EXEDIR\${TV_FILE}" "" "$EXEDIR\${TV_FILE}" 0
SetFileAttributes "TeamViewerQS_ru.exe" HIDDEN
Exec ${TV_FILE}
;ExitNow:
;MessageBox MB_OK "EXIT"
Return
SectionEnd
