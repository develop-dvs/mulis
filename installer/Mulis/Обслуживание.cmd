color 0f
echo off
cls
echo 嬪様様様様様様様様様様様様様冤様様様様様様様様様様冤様様様様様様様様様様様様様�
echo �                           � Mulis Services Tool �                           �
echo �                           青陳陳陳陳陳陳陳陳陳陳�                           �
echo �                                                                             �
echo 団陳 ��� ��┘��� 陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳�            �
echo � 1: ��お�鈑�碎 瓮皀〓� え瓷 [X:] � 甌Г�碎 閠�覈 �� ��｀腑� 痰���            �
echo � 2: �皖�鈑�碎 瓮皀〓� え瓷 [X:]                                              �
echo � 3: ���竅皋碎 �牀������ か� 磴��キ���� ��お�鈑キ��                           �
echo � 4: �����碎 �ム甌���讚襯 ��痰牀���                                           �
echo � 5: �����碎 ▲瘡 �辷                                                         �
echo � 6: ���竅皋碎 �牀▲爲� ��                                                    �
echo �                                                                             �
echo 団陳 ��� cム▲�� 陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳陳�            �
echo � 9: �ぅ��碎 ����� Mulis ぎ痰祚��� �� 瓮皋                                    �
echo � 0: �÷�碎 ����� Mulis ├ �♂ィ� ぎ痰祚�                    敖陳陳陳陳陳陳陳調
echo �                                                            � Divasoft, inc. �
echo 突様様様様様様様様様様様様様様様様様様様様様様様様様様様様様溶様様様様様様様様�

REM 憶鯵 � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � 桀毳�  � � �\
REM � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � �

set /p vibor="� �襦��: "
if "%vibor%"=="1" goto add_lan
if "%vibor%"=="2" goto del_lan
if "%vibor%"=="3" goto run_help
if "%vibor%"=="4" goto remove_config
if "%vibor%"=="5" goto remove_cache
if "%vibor%"=="6" goto repair_base

if "%vibor%"=="9" goto share
if "%vibor%"=="0" goto del_share


:remove_config
del %USERPROFILE%\.mulis\global.xml
goto exit

:remove_cache
RMDIR /S /Q %USERPROFILE%\.mulis\
goto exit

:run_help
start bin\Support.exe
goto exit

:add_lan
start "��Г�碎_瓮皀〓�_え瓷_�_閠�覈.exe"
goto exit

:del_lan
net use x: /delete
goto exit

:share
set TARGET_PATH=%~dp0
net share Mulis=%TARGET_PATH:~0,-1% /unlimited /grant:�瓮,full
net share Mulis=%TARGET_PATH:~0,-1% /unlimited /grant:Everyone,full
net share Mulis=%TARGET_PATH:~0,-1% /unlimited
goto exit

:del_share
set TARGET_PATH=%~dp0
net share Mulis /delete
goto exit

:repair_base
echo:
echo ��Ν�! Mulis ぎ�Ε� °碎 ���琺�
GOTO lp1

SET /p userinp=蹍ぎ�Θ碎 [Y/N] 
SET userinp=%userinp:~0,1%
IF "%userinp%"=="y" GOTO :lp1
IF "%userinp%"=="Y" GOTO :lp1
echo �Кキキ┤ �� �牀├�茫�
GOTO exit

:lp1
setlocal ENABLEDELAYEDEXPANSION
%~d0
cd %~dp0/bin/db/
mkdir bk
echo off
REM SET BK_DIR=bk/%date:~10,4%%date:~4,2%%date:~7,2%_%time:~0,2%%time:~3,2%%time:~6,2%
SET BK_DIR=bk\%date:~0,2%%date:~3,2%%date:~6,4%_%time:~0,2%%time:~3,2%%time:~6,2%
mkdir %BK_DIR%
for %%X in (*.db) do (
echo %BK_DIR%
	SET NAME=%%~nX
	SET DATABASE=!NAME!.db
	SET BACKUP=!NAME!.bk.db
	SET TEMP=temp_!NAME!.db

	echo ====================================
	echo ��瘁����←キ┘ �� !NAME!
	echo ====================================
	
	copy !DATABASE! !BACKUP!
	
	IF EXIST !TEMP! del !TEMP!
	sqlite3.exe !BACKUP! .dump | sqlite3.exe !TEMP!
	
	echo ����牀���┘ '!TEMP!' � '!DATABASE!'
	copy !TEMP! !DATABASE!
	del !TEMP!
	move !BACKUP! %BK_DIR%
	echo ��▲琥キ�!
	echo:
)
cls
color 0A
echo ====================================
echo ��瘁����←キ┘ ≡ュ �� ��▲琥キ�
echo �ェム↓�� �����: %BK_DIR%
echo ====================================
goto exit

:launch
start bin/Mulis.exe

:exit
pause
exit