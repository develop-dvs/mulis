; Taken from http://nsis.sourceforge.net/Simple_installer_with_JRE_check by weebib
; Use it as you desire.
 
; Credit given to so many people of the NSIS forum.
 
!define AppName "Mulis"
!define AppVersion "0.0.0.2"
!define ShortName "Mulis"
!define JRE_VERSION "1.6.0"
!define Vendor "Divasoft, inc."
 
!include "MUI.nsh"
!include "Sections.nsh"

Var InstallJRE
Var JREPath
Var SLASH

 AllowRootDirInstall true
 ;XPStyle on

;--------------------------------
;Configuration
 
  ;General
  Name "${AppName}"
  OutFile "build-${AppVersion}-full.exe"
 
  ;Folder selection page
  InstallDir "c:\${SHORTNAME}"
 
  ;Get install folder from registry if available
  InstallDirRegKey HKLM "SOFTWARE\${Vendor}\${ShortName}" ""
 
; Installation types
;InstType "full"	; Uncomment if you want Installation types
 
;--------------------------------
;Pages
 
  ; License page
  ;!insertmacro MUI_PAGE_LICENSE "${NSISDIR}\Contrib\Modern UI\License.txt"
  ; This page checks for JRE. It displays a dialog based on JRE.ini if it needs to install JRE
  ; Otherwise you won't see it.
  Page custom CheckInstalledJRE
 
  ; Define headers for the 'Java installation successfully' page
  !define MUI_INSTFILESPAGE_FINISHHEADER_TEXT "��������� Java ���������"
  !define MUI_PAGE_HEADER_TEXT "��������� Java Runtime 7"
  !define MUI_PAGE_HEADER_SUBTEXT "����������, ���������, ���� ��������� JRE"
  !define MUI_INSTFILESPAGE_FINISHHEADER_SUBTEXT "JRE 7 ������� �����������."
  !insertmacro MUI_PAGE_INSTFILES
  !define MUI_INSTFILESPAGE_FINISHHEADER_TEXT "��������� ���������"
  !define MUI_PAGE_HEADER_TEXT "���������"
  !define MUI_PAGE_HEADER_SUBTEXT "����������, ���������, ${AppName} ���������������."
; Uncomment the next line if you want optional components to be selectable
  ;!insertmacro MUI_PAGE_COMPONENTS
  !define MUI_PAGE_CUSTOMFUNCTION_PRE myPreInstfiles
  !define MUI_PAGE_CUSTOMFUNCTION_LEAVE RestoreSections
  !define MUI_FINISHPAGE_RUN "$INSTDIR\bin\${AppName}.exe"
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
 
;--------------------------------
;Modern UI Configuration
 
  !define MUI_ABORTWARNING
 
;--------------------------------
;Languages
 
  ;!insertmacro MUI_LANGUAGE "English"
  !insertmacro MUI_LANGUAGE "Russian"
 
;--------------------------------
;Language Strings
 
  ;Description
  LangString DESC_SecAppFiles ${LANG_RUSSIAN} "��������� ��������� ������"
  LangString DESC_SecAppLibFiles ${LANG_RUSSIAN} "��������� ������ ��������"
  LangString DESC_SecAppDbFiles ${LANG_RUSSIAN} "��������� ������ ��� ������"
  LangString DESC_SecCreateShortcut ${LANG_RUSSIAN} "�������� �������"
 
  ;Header
  LangString TEXT_JRE_TITLE ${LANG_RUSSIAN} "Java Runtime Environment"
  LangString TEXT_JRE_SUBTITLE ${LANG_RUSSIAN} "���������"
  LangString TEXT_PRODVER_TITLE ${LANG_RUSSIAN} "����������� ������ ${AppName}"
  LangString TEXT_PRODVER_SUBTITLE ${LANG_RUSSIAN} "��������� ��������"
 
;--------------------------------
;Reserve Files
 
  ;Only useful for BZIP2 compression
 
 
  ReserveFile "jre.ini"
  !insertmacro MUI_RESERVEFILE_INSTALLOPTIONS
 
;--------------------------------
;Installer Sections
 
Section -installjre jre
  Push $0
  Push $1
 
;  MessageBox MB_OK "Inside JRE Section"
  Strcmp $InstallJRE "yes" InstallJRE JREPathStorage
  DetailPrint "������ ��������� JRE"
InstallJRE:
  File /oname=$TEMP\jre_setup.exe j2re-setup.exe
  ;MessageBox MB_OK "��������� JRE"
  DetailPrint "��������� JRE"
  ;ExecWait "$TEMP\jre_setup.exe /S" $0
  ; The silent install /S does not work for installing the JRE, sun has documentation on the 
  ; parameters needed.  I spent about 2 hours hammering my head against the table until it worked
  ExecWait '"$TEMP\jre_setup.exe" /s /v\"/qn REBOOT=Suppress JAVAUPDATE=0 WEBSTARTICON=0\"' $0
  DetailPrint "��������� ���������"
  Delete "$TEMP\jre_setup.exe"
  StrCmp $0 "0" InstallVerif 0
  Push "��������� JRE ���� �������� �� �� ��������� ������."
  Goto ExitInstallJRE
 
InstallVerif:
  DetailPrint "�������� ������������ ��������� JRE"
;  MessageBox MB_OK "Checking JRE outcome"
  Push "${JRE_VERSION}"
  Call DetectJRE  
  Pop $0	  ; DetectJRE's return value
  StrCmp $0 "0" ExitInstallJRE 0
  StrCmp $0 "-1" ExitInstallJRE 0
  Goto JavaExeVerif
  Push "������ ����� ��������� JRE"
  Goto ExitInstallJRE
 
JavaExeVerif:
  IfFileExists $0 JREPathStorage 0
  Push "���� : $0, �� ������."
  Goto ExitInstallJRE
 
JREPathStorage:
;  MessageBox MB_OK "Path Storage"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "UserDefinedSection" "JREPath" $1
  StrCpy $JREPath $0
  Goto End
 
ExitInstallJRE:
  Pop $1
  MessageBox MB_OK "�������� ��������, �� ������� : $1"
  Pop $1 	; Restore $1
  Pop $0 	; Restore $0
  Abort
End:
  Pop $1	; Restore $1
  Pop $0	; Restore $0
SectionEnd
 
Section "��������� ${AppName}" SecAppFiles
  SectionIn 1 RO	; Full install, cannot be unselected
			; If you add more sections be sure to add them here as well
StrCpy $SLASH $INSTDIR 2
StrCmp $SLASH "x:" AnormalPatch ExtractFiles
AnormalPatch:
  StrCpy $INSTDIR "x:\"
ExtractFiles:
SetOutPath $INSTDIR

File /r "Mulis\"
;File "Mulis\Mulis.exe"
;File "Mulis\Mulis.jar"
;File "Mulis\repair.cmd"
; If you need the path to JRE, you can either get it here for from $JREPath
;  !insertmacro MUI_INSTALLOPTIONS_READ $0 "jre.ini" "UserDefinedSection" "JREPath"
;  MessageBox MB_OK "JRE Read: $0"
  ;Store install folder
  WriteRegStr HKLM "SOFTWARE\${Vendor}\${ShortName}" "" $INSTDIR
 
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ShortName}" "DisplayName" "${AppName}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ShortName}" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ShortName}" "NoModify" "1"
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ShortName}" "NoRepair" "1"
 
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
	SetFileAttributes "$INSTDIR\Uninstall.exe" HIDDEN
	;SetFileAttributes "$INSTDIR\link.vbs" HIDDEN
SectionEnd

Section "��������� ������ ������������" SecAppLibFiles
;MessageBox MB_OK "SecAppLibFiles"
  SectionIn 1	; Can be unselected
  ;SetOutPath $INSTDIR
;File /r "Mulis\db_xml" 
SectionEnd

;Section "��������� ������ ��� ������" SecAppDbFiles
;MessageBox MB_OK "SecAppDbFiles"
;  SectionIn 1	; Can be unselected
;  SetOutPath $INSTDIR
;File /r "Mulis\db" 
;SectionEnd

Section "������� ������" SecCreateShortcut
  SectionIn 1	; Can be unselected
  RMDir /r "$SMPROGRAMS\${AppName}"
  Delete "$DESKTOP\${AppName}.lnk"
  CreateDirectory "$SMPROGRAMS\${AppName}"
  SetOutPath "$INSTDIR\bin"
  CreateShortCut "$SMPROGRAMS\${AppName}\${AppName}.lnk" "$INSTDIR\bin\${AppName}.exe" "" "$INSTDIR\bin\${AppName}.exe" 0
  CreateShortCut "$SMPROGRAMS\${AppName}\��������� �����������.lnk" "$INSTDIR\bin\Support.exe" "" "$INSTDIR\bin\Support.exe" 0
  SetOutPath "$INSTDIR"
  CreateShortCut "$SMPROGRAMS\${AppName}\������������.lnk" "$INSTDIR\������������.cmd" "" "$INSTDIR\������������.cmd" 0
  CreateShortCut "$SMPROGRAMS\${AppName}\�������.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$DESKTOP\${AppName}.lnk" "$INSTDIR\bin\${AppName}.exe" "" "$INSTDIR\bin\${AppName}.exe" 0
; Etc
SectionEnd
 
;--------------------------------
;Descriptions
 
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SecAppFiles} $(DESC_SecAppFiles)
  !insertmacro MUI_DESCRIPTION_TEXT ${SecAppLibFiles} $(DESC_SecAppLibFiles)
;  !insertmacro MUI_DESCRIPTION_TEXT ${SecAppDbFiles} $(DESC_SecAppDbFiles)
  !insertmacro MUI_DESCRIPTION_TEXT ${SecCreateShortcut} $(DESC_SecCreateShortcut)
!insertmacro MUI_FUNCTION_DESCRIPTION_END
 
;--------------------------------
;Installer Functions
 
Function .onInit
 
  ;Extract InstallOptions INI Files
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "jre.ini"
   IfFileExists "x:\Uninstall.exe" AnormalPatchX ExtractFilesX
AnormalPatchX:
  StrCpy $INSTDIR "x:\"
  SetOutPath $INSTDIR
ExtractFilesX:

  Call SetupSections
 
FunctionEnd
 
Function myPreInstfiles
 
  Call RestoreSections
  SetAutoClose true
 
FunctionEnd
 
Function CheckInstalledJRE
  ;MessageBox MB_OK "�������� ������������� ������ JRE"
  Push "${JRE_VERSION}"
  Call DetectJRE
  ;Messagebox MB_OK "�������� ������ JRE ���������"
  Exch $0	; Get return value from stack
  StrCmp $0 "0" NoFound
  StrCmp $0 "-1" FoundOld
  Goto JREAlreadyInstalled
 
FoundOld:
  ;MessageBox MB_OK "��������� ������ JRE"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "Field 1" "Text" "��� ${AppName} ��������� ����� JRE. ��������� ����� ������ JRE ${JRE_VERSION} ��������."
  !insertmacro MUI_HEADER_TEXT "$(TEXT_JRE_TITLE)" "$(TEXT_JRE_SUBTITLE)"
  !insertmacro MUI_INSTALLOPTIONS_DISPLAY_RETURN "jre.ini"
  Goto MustInstallJRE
 
NoFound:
  ;MessageBox MB_OK "JRE �� ���������."
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "Field 1" "Text" "��� ${AppName} ��������� Java Runtime Environment(JRE). ��������� JRE v${JRE_VERSION} ��������."
  !insertmacro MUI_HEADER_TEXT "$(TEXT_JRE_TITLE)" "$(TEXT_JRE_SUBTITLE)"
  !insertmacro MUI_INSTALLOPTIONS_DISPLAY_RETURN "jre.ini"
  Goto MustInstallJRE
 
MustInstallJRE:
  Exch $0	; $0 now has the installoptions page return value
  ; Do something with return value here
  Pop $0	; Restore $0
  StrCpy $InstallJRE "yes"
  Return
 
JREAlreadyInstalled:
;  MessageBox MB_OK "No download: ${TEMP2}"
  ;MessageBox MB_OK "JRE ��� ����������"
  StrCpy $InstallJRE "no"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "UserDefinedSection" "JREPath" $JREPATH
  Pop $0		; Restore $0
  Return
 
FunctionEnd
 
; Returns: 0 - JRE not found. -1 - JRE found but too old. Otherwise - Path to JAVA EXE
 
; DetectJRE. Version requested is on the stack.
; Returns (on stack)	"0" on failure (java too old or not installed), otherwise path to java interpreter
; Stack value will be overwritten!
 
Function DetectJRE
  Exch $0	; Get version requested  
		; Now the previous value of $0 is on the stack, and the asked for version of JDK is in $0
  Push $1	; $1 = Java version string (ie 1.5.0)
  Push $2	; $2 = Javahome
  Push $3	; $3 and $4 are used for checking the major/minor version of java
  Push $4
  ;MessageBox MB_OK "Detecting JRE"
  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ;MessageBox MB_OK "Read : $1"
  StrCmp $1 "" DetectTry2
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
  ;MessageBox MB_OK "Read 3: $2"
  StrCmp $2 "" DetectTry2
  Goto GetJRE
 
DetectTry2:
  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  ;MessageBox MB_OK "Detect Read : $1"
  StrCmp $1 "" NoFound
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$1" "JavaHome"
  ;MessageBox MB_OK "Detect Read 3: $2"
  StrCmp $2 "" NoFound
 
GetJRE:
; $0 = version requested. $1 = version found. $2 = javaHome
  ;MessageBox MB_OK "Getting JRE"
  IfFileExists "$2\bin\java.exe" 0 NoFound
  StrCpy $3 $0 1			; Get major version. Example: $1 = 1.5.0, now $3 = 1
  StrCpy $4 $1 1			; $3 = major version requested, $4 = major version found
  ;MessageBox MB_OK "Want $3 , found $4"
  IntCmp $4 $3 0 FoundOld FoundNew
  StrCpy $3 $0 1 2
  StrCpy $4 $1 1 2			; Same as above. $3 is minor version requested, $4 is minor version installed
  ;MessageBox MB_OK "Want $3 , found $4" 
  IntCmp $4 $3 FoundNew FoundOld FoundNew
 
NoFound:
  ;MessageBox MB_OK "JRE not found"
  Push "0"
  Goto DetectJREEnd
 
FoundOld:
  ;MessageBox MB_OK "JRE too old: $3 is older than $4"
;  Push ${TEMP2}
  Push "-1"
  Goto DetectJREEnd  
FoundNew:
  ;MessageBox MB_OK "JRE is new: $3 is newer than $4"
 
  Push "$2\bin\java.exe"
;  Push "OK"
;  Return
   Goto DetectJREEnd
DetectJREEnd:
	; Top of stack is return value, then r4,r3,r2,r1
	Exch	; => r4,rv,r3,r2,r1,r0
	Pop $4	; => rv,r3,r2,r1r,r0
	Exch	; => r3,rv,r2,r1,r0
	Pop $3	; => rv,r2,r1,r0
	Exch 	; => r2,rv,r1,r0
	Pop $2	; => rv,r1,r0
	Exch	; => r1,rv,r0
	Pop $1	; => rv,r0
	Exch	; => r0,rv
	Pop $0	; => rv 
FunctionEnd
 
Function RestoreSections
  !insertmacro UnselectSection ${jre}
  !insertmacro SelectSection ${SecAppFiles}
  !insertmacro SelectSection ${SecAppLibFiles}
  ;!insertmacro SelectSection ${SecAppDbFiles}
  !insertmacro SelectSection ${SecCreateShortcut}
 
FunctionEnd
 
Function SetupSections
  !insertmacro SelectSection ${jre}
  !insertmacro UnselectSection ${SecAppFiles}
  !insertmacro UnselectSection ${SecAppLibFiles}
  ;!insertmacro UnselectSection ${SecAppDbFiles}
  !insertmacro UnselectSection ${SecCreateShortcut}
FunctionEnd
 
;--------------------------------
;Uninstaller Section
 
Section "Uninstall"
 
  ; remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ShortName}"
  DeleteRegKey HKLM  "SOFTWARE\${Vendor}\${AppName}"
  ; remove shortcuts, if any.
  Delete "$SMPROGRAMS\${AppName}\*.*"
  Delete "$PROFILE\.mulis\*.*"
  Delete "$DESKTOP\${AppName}.lnk"
  ; remove files
  RMDir /r "$INSTDIR"
  RMDir /r "$PROFILE\.mulis"
 
SectionEnd