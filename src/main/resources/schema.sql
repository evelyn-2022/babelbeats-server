-- Create AppUser Table
CREATE TABLE IF NOT EXISTS app_user
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255),
    email       VARCHAR(255) UNIQUE,
    profile_pic VARCHAR(255),
    cognito_sub VARCHAR(255),
    provider_id VARCHAR(255)
);

-- Create Index for providerId on AppUser Table
CREATE INDEX IF NOT EXISTS idx_provider_id ON app_user (provider_id);

-- Create Playlist Table
CREATE TABLE IF NOT EXISTS playlists
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create Music Table
CREATE TABLE IF NOT EXISTS music
(
    id     SERIAL PRIMARY KEY,
    ytb_id VARCHAR(255) NOT NULL,  -- Matches the ytbId field in your Music entity
    lyrics TEXT                    -- Matches the lyrics field in your Music entity
);

-- Create Index for ytb_id on Music Table
CREATE INDEX IF NOT EXISTS idx_ytb_id ON music (ytb_id);

-- Create Join Table for Many-to-Many Relationship
CREATE TABLE IF NOT EXISTS playlist_music
(
    playlist_id BIGINT NOT NULL,
    music_id    BIGINT NOT NULL,
    PRIMARY KEY (playlist_id, music_id),
    FOREIGN KEY (playlist_id) REFERENCES playlists (id),
    FOREIGN KEY (music_id) REFERENCES music (id)
);
