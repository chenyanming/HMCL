/*
 * Hello Minecraft! Launcher.
 * Copyright (C) 2018  huangyuhui <huanghongxun2008@126.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */
package org.jackhuang.hmcl.download.game;

import org.jackhuang.hmcl.download.DownloadProvider;
import org.jackhuang.hmcl.download.RemoteVersion;
import org.jackhuang.hmcl.download.VersionList;
import org.jackhuang.hmcl.task.GetTask;
import org.jackhuang.hmcl.task.Task;
import org.jackhuang.hmcl.util.Constants;
import org.jackhuang.hmcl.util.NetworkUtils;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author huangyuhui
 */
public final class GameVersionList extends VersionList<GameRemoteVersionTag> {

    public static final GameVersionList INSTANCE = new GameVersionList();

    private GameVersionList() {
    }

    @Override
    public Task refreshAsync(DownloadProvider downloadProvider) {
        GetTask task = new GetTask(NetworkUtils.toURL(downloadProvider.getVersionListURL()));
        return new Task() {
            @Override
            public Collection<Task> getDependents() {
                return Collections.singleton(task);
            }

            @Override
            public void execute() {
                lock.writeLock().lock();

                try {
                    versions.clear();

                    GameRemoteVersions root = Constants.GSON.fromJson(task.getResult(), GameRemoteVersions.class);
                    for (GameRemoteVersion remoteVersion : root.getVersions()) {
                        versions.put(remoteVersion.getGameVersion(), new RemoteVersionGame(
                                remoteVersion.getGameVersion(),
                                remoteVersion.getGameVersion(),
                                remoteVersion.getUrl(),
                                new GameRemoteVersionTag(remoteVersion.getType(), remoteVersion.getReleaseTime()))
                        );
                    }
                } finally {
                    lock.writeLock().unlock();
                }
            }
        };
    }

    private static class RemoteVersionGame extends RemoteVersion<GameRemoteVersionTag> {
        public RemoteVersionGame(String gameVersion, String selfVersion, String url, GameRemoteVersionTag tag) {
            super(gameVersion, selfVersion, url, tag);
        }

        @Override
        public int compareTo(RemoteVersion<GameRemoteVersionTag> o) {
            return o.getTag().getTime().compareTo(getTag().getTime());
        }
    }
}
