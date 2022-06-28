; Java Launcher with automatic JRE installation
; >"C:\Program Files (x86)\Java\jre7\bin\javaw.exe" -classpath "Mulis.jar"
;-----------------------------------------------
 
Name "Mulis Launcher"
Caption "Mulis Launcher"
Icon "Mulis.ico"
OutFile "Mulis\bin\Mulis.exe"
 
VIAddVersionKey "ProductName" "Mulis Launcher"
VIAddVersionKey "Comments" "Mulis for Windows"
VIAddVersionKey "CompanyName" "Divasoft, inc."
VIAddVersionKey "LegalTrademarks" "Mulis Launcher is a trademark of Divasoft, inc."
VIAddVersionKey "LegalCopyright" "Divasoft, inc."
VIAddVersionKey "FileDescription" "Mulis Launcher"
VIAddVersionKey "FileVersion" "1.0.0"
VIProductVersion "0.0.0.2"
 
!define CLASSPATH "Mulis.jar"
!define CLASS "mulis.MulisApp.class"
!define PRODUCT_NAME "Mulis"
 
; Definitions for Java 7.0
!define JRE_VERSION "7.0"
!define JRE_URL "http://javadl.sun.com/webapps/download/AutoDL?BundleId=76860&/jre-7u21-windows-i586.exe"
;!define JRE_VERSION "6.0"
;!define JRE_URL "http://javadl.sun.com/webapps/download/AutoDL?BundleId=24936&/jre-6u10-windows-i586-p.exe"
;!define JRE_VERSION "5.0"
;!define JRE_URL "http://javadl.sun.com/webapps/download/AutoDL?BundleId=22933&/jre-1_5_0_16-windows-i586-p.exe"
 
; use javaw.exe to avoid dosbox.
; use java.exe to keep stdout/stderr
!define JAVAEXE "javaw.exe"
 
RequestExecutionLevel user
SilentInstall normal
AutoCloseWindow true
ShowInstDetails show
 
!include "FileFunc.nsh"
!insertmacro GetFileVersion
!insertmacro GetParameters
!include "WordFunc.nsh"
!insertmacro VersionCompare
 
Section ""
  Call GetJRE
  Pop $R0
 
  ; change for your purpose (-jar etc.)
  ${GetParameters} $1
  ;StrCpy $0 '"$R0" -classpath "${CLASSPATH}" ${CLASS} $1'
  StrCpy $0 '"$R0" -Dfile.encoding="UTF-8" -jar "${CLASSPATH}" $1'
 
  SetOutPath $EXEDIR
  ;MessageBox MB_OK $0
  Exec $0
SectionEnd
 
;  returns the full path of a valid java.exe
;  looks in:
;  1 - .\jre directory (JRE Installed with application)
;  2 - JAVA_HOME environment variable
;  3 - the registry
;  4 - hopes it is in current dir or PATH
Function GetJRE
    Push $R0
    Push $R1
    Push $2
 
  ; 1) Check local JRE
  CheckLocal:
    ClearErrors
    StrCpy $R0 "$EXEDIR\jre\bin\${JAVAEXE}"
    IfFileExists $R0 JreFound
 
  ; 2) Check for JAVA_HOME
  CheckJavaHome:
    ClearErrors
    ReadEnvStr $R0 "JAVA_HOME"
    StrCpy $R0 "$R0\bin\${JAVAEXE}"
    IfErrors CheckRegistry     
    IfFileExists $R0 0 CheckRegistry
    Call CheckJREVersion
    IfErrors CheckRegistry JreFound
 
  ; 3) Check for registry
  CheckRegistry:
    ClearErrors
    ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
    StrCpy $R0 "$R0\bin\${JAVAEXE}"
    IfErrors DownloadJRE
    IfFileExists $R0 0 DownloadJRE
    Call CheckJREVersion
    IfErrors DownloadJRE JreFound
 
  DownloadJRE:
	# Get the OS version
	#nsisos::osversion
	#StrCpy $R0 $0
	#StrCpy $R1 $1
	#StrCmp $R0 "5" JustInstall
	#Call ElevateToAdmin
	#JustInstall:
    MessageBox MB_ICONINFORMATION "${PRODUCT_NAME} использует Java Runtime Environment ${JRE_VERSION}, сейчас начнетс€ загрузка необходимых библиотек."
    StrCpy $2 "$TEMP\Java Runtime Environment.exe"
    nsisdl::download /TIMEOUT=30000 ${JRE_URL} $2
    Pop $R0 ;Get the return value
    StrCmp $R0 "success" +3
      MessageBox MB_ICONSTOP "«агрузка прервана: $R0"
      Abort
    ExecWait $2
    Delete $2
 
    ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
    StrCpy $R0 "$R0\bin\${JAVAEXE}"
    IfFileExists $R0 0 GoodLuck
    Call CheckJREVersion
    IfErrors GoodLuck JreFound
 
  ; 4) wishing you good luck
  GoodLuck:
    StrCpy $R0 "${JAVAEXE}"
    ; MessageBox MB_ICONSTOP "Cannot find appropriate Java Runtime Environment."
    ; Abort
 
  JreFound:
    Pop $2
    Pop $R1
    Exch $R0
FunctionEnd
 
; Pass the "javaw.exe" path by $R0
Function CheckJREVersion
    Push $R1
 
    ; Get the file version of javaw.exe
    ${GetFileVersion} $R0 $R1
    ${VersionCompare} ${JRE_VERSION} $R1 $R1
 
    ; Check whether $R1 != "1"
    ClearErrors
    StrCmp $R1 "1" 0 CheckDone
    SetErrors
 
  CheckDone:
    Pop $R1
FunctionEnd