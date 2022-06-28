color 0f
echo off
cls
echo �����������������������������������������������������������������������������͸
echo �                           � Mulis Services Tool �                           �
echo �                           �����������������������                           �
echo �                                                                             �
echo ���� ��� ������ �������������������������������������������������            �
echo � 1: ��������� �⥢�� ��� [X:] � ᮧ���� ��� �� ࠡ�祬 �⮫�            �
echo � 2: �⪫���� �⥢�� ��� [X:]                                              �
echo � 3: �������� �ணࠬ�� ��� 㤠������� ������祭��                           �
echo � 4: ������� ���ᮭ���� ����ன��                                           �
echo � 5: ������� ���� ���                                                         �
echo � 6: �������� �஢��� ��                                                    �
echo �                                                                             �
echo ���� ��� c�ࢥ� �������������������������������������������������            �
echo � 9: ������� ����� Mulis ����㯭�� �� ��                                    �
echo � 0: ����� ����� Mulis �� ��饣� ����㯠                    ����������������Ĵ
echo �                                                            � Divasoft, inc. �
echo �����������������������������������������������������������������������������;

REM ���� � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � �����  � � �\
REM � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � �

set /p vibor="� �롮�: "
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
start "�������_�⥢��_���_�_���.exe"
goto exit

:del_lan
net use x: /delete
goto exit

:share
set TARGET_PATH=%~dp0
net share Mulis=%TARGET_PATH:~0,-1% /unlimited /grant:��,full
net share Mulis=%TARGET_PATH:~0,-1% /unlimited /grant:Everyone,full
net share Mulis=%TARGET_PATH:~0,-1% /unlimited
goto exit

:del_share
set TARGET_PATH=%~dp0
net share Mulis /delete
goto exit

:repair_base
echo:
echo �����! Mulis ������ ���� ������
GOTO lp1

SET /p userinp=�த������ [Y/N] 
SET userinp=%userinp:~0,1%
IF "%userinp%"=="y" GOTO :lp1
IF "%userinp%"=="Y" GOTO :lp1
echo ��������� �� �ந��諮
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
	echo ����⠭������� �� !NAME!
	echo ====================================
	
	copy !DATABASE! !BACKUP!
	
	IF EXIST !TEMP! del !TEMP!
	sqlite3.exe !BACKUP! .dump | sqlite3.exe !TEMP!
	
	echo ����஢���� '!TEMP!' � '!DATABASE!'
	copy !TEMP! !DATABASE!
	del !TEMP!
	move !BACKUP! %BK_DIR%
	echo �����襭�!
	echo:
)
cls
color 0A
echo ====================================
echo ����⠭������� ��� �� �����襭�
echo ����ࢭ�� �����: %BK_DIR%
echo ====================================
goto exit

:launch
start bin/Mulis.exe

:exit
pause
exit