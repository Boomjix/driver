# Copyright (C) 2017 - present Juergen Zimmermann, Hochschule Karlsruhe
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>

Param (
    [string]$cmd = "start"
)

Set-StrictMode -Version Latest

# Titel setzen
$script = $myInvocation.MyCommand.Name
$host.ui.RawUI.WindowTitle = $script

function StartServer {
    runas /user:$env:COMPUTERNAME\postgres "C:\Zimmermann\pgsql\bin\pg_ctl --pgdata=C:\Zimmermann\pgsql\data start"
}

function StopServer {
    runas /user:$env:COMPUTERNAME\postgres "C:\Zimmermann\pgsql\bin\pg_ctl --pgdata=C:\Zimmermann\pgsql\data stop"
}

switch ($cmd) {
    "start" { StartServer; break }
    "stop" { StopServer; break }
    "shutdown" { StopServer; break }
    default { write-host "$script [cmd=start|stop|shutdown]" }
}
