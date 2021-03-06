/*
 * Copyright 2016 Lukáš Petrovický
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.triceo.robozonky.app.authentication;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Optional;

import com.github.triceo.robozonky.app.util.KeyStoreHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Every set*() operation must result in a {@link KeyStoreHandler#save()} call.
 */
class KeyStoreInformationProvider extends SensitiveInformationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyStoreInformationProvider.class);
    private static final String ALIAS_PASSWORD = "pwd";
    private static final String ALIAS_USERNAME = "usr";
    private static final String ALIAS_TOKEN = "tkn";
    private static final String ALIAS_TOKEN_DATE = "tknd";

    private final KeyStoreHandler ksh;

    public KeyStoreInformationProvider(final KeyStoreHandler ksh) {
        if (ksh == null) {
            throw new IllegalArgumentException("KeyStoreHandler must be provided.");
        }
        this.ksh = ksh;
    }

    /**
     * Set a key in the key store.
     *
     * @param alias Alias to store the key under.
     * @param valueStream Will be converted to a {@link String} and stored.
     * @return True if success.
     */
    private boolean set(final String alias, final Reader valueStream) {
        try {
            return this.set(alias, IOUtils.toString(valueStream));
        } catch (final IOException ex) {
            KeyStoreInformationProvider.LOGGER.warn("Failed saving keystore.", ex);
            return false;
        }
    }

    /**
     * Set a key in the key store.
     *
     * @param alias Alias to store the key under.
     * @param value Will be stored.
     * @return True if success.
     */
    private boolean set(final String alias, final String value) {
        try {
            final boolean result = this.ksh.set(alias, value);
            this.ksh.save();
            return result;
        } catch (final IOException ex) {
            KeyStoreInformationProvider.LOGGER.warn("Failed saving keystore.", ex);
            return false;
        }
    }

    @Override
    public String getPassword() {
        return this.ksh.get(KeyStoreInformationProvider.ALIAS_PASSWORD)
                .orElseThrow(() -> new IllegalStateException("Password not present in KeyStore."));
    }

    @Override
    public String getUsername() {
        return this.ksh.get(KeyStoreInformationProvider.ALIAS_USERNAME)
                .orElseThrow(() -> new IllegalStateException("Username not present in KeyStore."));
    }

    public boolean setPassword(final String password) {
        return this.set(KeyStoreInformationProvider.ALIAS_PASSWORD, password);
    }

    public boolean setUsername(final String username) {
        return this.set(KeyStoreInformationProvider.ALIAS_USERNAME, username);
    }

    @Override
    public Optional<Reader> getToken() {
        final Optional<String> stored = this.ksh.get(KeyStoreInformationProvider.ALIAS_TOKEN);
        if (stored.isPresent()) {
            return Optional.of(new StringReader(stored.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean setToken(final Reader token) {
        final boolean firstSuccessful = this.set(KeyStoreInformationProvider.ALIAS_TOKEN, token);
        final boolean secondSuccessful = this.set(KeyStoreInformationProvider.ALIAS_TOKEN_DATE,
                LocalDateTime.now().toString());
        return firstSuccessful && secondSuccessful;
    }

    @Override
    public boolean setToken() {
        boolean result = this.ksh.delete(KeyStoreInformationProvider.ALIAS_TOKEN);
        result = this.ksh.delete(KeyStoreInformationProvider.ALIAS_TOKEN_DATE) && result;
        try {
            this.ksh.save();
            return result;
        } catch (final IOException ex) {
            KeyStoreInformationProvider.LOGGER.warn("Failed saving keystore.", ex);
            return false;
        }
    }

    @Override
    public Optional<LocalDateTime> getTokenSetDate() {
        return this.ksh.get(KeyStoreInformationProvider.ALIAS_TOKEN_DATE)
                .map(s -> Optional.of(LocalDateTime.parse(s)))
                .orElse(Optional.empty());
    }
}
