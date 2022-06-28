color 0f
echo off
cls
echo ╒═══════════════════════════╤═════════════════════╤═══════════════════════════╕
echo │                           │ Mulis Services Tool │                           │
echo │                           └─────────────────────┘                           │
echo │                                                                             │
echo ├─── Для клиента ─────────────────────────────────────────────────            │
echo │ 1: Подключить сетевой диск [X:] и создать ярлык на рабочем столе            │
echo │ 2: Отключить сетевой диск [X:]                                              │
echo │ 3: Запустить программу для удаленного подключения                           │
echo │ 4: Удалить персональные настройки                                           │
echo │ 5: Удалить весь кэш                                                         │
echo │ 6: Запустить проверку БД                                                    │
echo │                                                                             │
echo ├─── Для cервера ─────────────────────────────────────────────────            │
echo │ 9: Сделать папку Mulis доступной по сети                                    │
echo │ 0: Убрать папку Mulis из общего доступа                    ┌────────────────┤
echo │                                                            │ Divasoft, inc. │
echo ╞════════════════════════════════════════════════════════════╧════════════════╛

REM ▓▒░│ ┤ ╡ ╢ ╖ ╕ ╣ ║ ╗ ╝ ╜ ╛ ┐ └ ┴ ┬ ├ ─ ┼ ╞ ╟ ╚ ╔ ╩ ╦ ╠ ═ ╬ ╧ ╨ ╤ ╥ ╙ ╒ ╓ ╫ ╪ ┘ ┌ █▄▌▐▀  └ ╙ ╚\
REM ░ ▒ ▓ │ ┤ ╡ ╢ ╖ ╕ ╣ ║ ╗ ╝ ╜ ╛ ┐ └ ┴ ┬ ├ ─ ┼ ╞ ╟ ╚ ╔ ╩ ╦ ╠ ═ ╬ ╧ ╨ ╤ ╥ ╙ ╘ ╒ ╓ ╫ ╪ ┘ ┌ █ ▄ ▌ ▐ ▀

set /p vibor="└ Выбор: "
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
start "Создать_сетевой_диск_и_ярлык.exe"
goto exit

:del_lan
net use x: /delete
goto exit

:share
set TARGET_PATH=%~dp0
net share Mulis=%TARGET_PATH:~0,-1% /unlimited /grant:Все,full
net share Mulis=%TARGET_PATH:~0,-1% /unlimited /grant:Everyone,full
net share Mulis=%TARGET_PATH:~0,-1% /unlimited
goto exit

:del_share
set TARGET_PATH=%~dp0
net share Mulis /delete
goto exit

:repair_base
echo:
echo Важно! Mulis должен быть закрыт
GOTO lp1

SET /p userinp=Продолжить [Y/N] 
SET userinp=%userinp:~0,1%
IF "%userinp%"=="y" GOTO :lp1
IF "%userinp%"=="Y" GOTO :lp1
echo Изменений не произошло
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
	echo Восстановление БД !NAME!
	echo ====================================
	
	copy !DATABASE! !BACKUP!
	
	IF EXIST !TEMP! del !TEMP!
	sqlite3.exe !BACKUP! .dump | sqlite3.exe !TEMP!
	
	echo Копирование '!TEMP!' в '!DATABASE!'
	copy !TEMP! !DATABASE!
	del !TEMP!
	move !BACKUP! %BK_DIR%
	echo Завершено!
	echo:
)
cls
color 0A
echo ====================================
echo Восстановление всех БД завершено
echo Резервная копия: %BK_DIR%
echo ====================================
goto exit

:launch
start bin/Mulis.exe

:exit
pause
exit