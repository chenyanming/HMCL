/*
 * Hello Minecraft! Launcher.
 * Copyright (C) 2017  huangyuhui <huanghongxun2008@126.com>
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
package org.jackhuang.hmcl.auth;

import java.net.Proxy;
import java.util.Objects;

public final class AccountBuilder<T extends Account> {
    private CharacterSelector selector = CharacterSelector.DEFAULT;
    private String username;
    private String password = null;
    private Object additionalData = null;
    private Proxy proxy = Proxy.NO_PROXY;

    public AccountBuilder() {
    }

    public AccountBuilder setSelector(CharacterSelector selector) {
        this.selector = Objects.requireNonNull(selector);
        return this;
    }

    public AccountBuilder setUsername(String username) {
        this.username = Objects.requireNonNull(username);
        return this;
    }

    public AccountBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public AccountBuilder setAdditionalData(Object additionalData) {
        this.additionalData = additionalData;
        return this;
    }

    public AccountBuilder setProxy(Proxy proxy) {
        this.proxy = Objects.requireNonNull(proxy);
        return this;
    }

    public T create(AccountFactory<T> factory) throws AuthenticationException {
        return factory.create(selector, Objects.requireNonNull(username), password, additionalData, proxy);
    }
}
