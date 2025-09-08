-- Indeksy poprawiające filtrowanie listy zgłoszeń
CREATE INDEX IF NOT EXISTS idx_zgloszenia_status ON zgloszenia(status);
CREATE INDEX IF NOT EXISTS idx_zgloszenia_priorytet ON zgloszenia(priorytet);
CREATE INDEX IF NOT EXISTS idx_zgloszenia_dzial_id ON zgloszenia(dzial_id);
CREATE INDEX IF NOT EXISTS idx_zgloszenia_autor_id ON zgloszenia(autor_id);