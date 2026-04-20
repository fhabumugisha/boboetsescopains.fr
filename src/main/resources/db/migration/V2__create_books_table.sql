-- V2: Create books table
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(200) NOT NULL UNIQUE,
    short_description TEXT,
    full_description TEXT,
    cover_image_url VARCHAR(500),
    cover_image_s3_key VARCHAR(500),
    price DECIMAL(10, 2),
    amazon_link VARCHAR(500),
    ebook_link VARCHAR(500),
    published_date TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    display_order INTEGER DEFAULT 0,
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    views_count BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX idx_books_slug ON books(slug);
CREATE INDEX idx_books_status ON books(status);
CREATE INDEX idx_books_featured ON books(featured);
CREATE INDEX idx_books_display_order ON books(display_order);
CREATE INDEX idx_books_created_at ON books(created_at);

-- Insert sample books data
INSERT INTO books (title, slug, short_description, full_description, cover_image_url, amazon_link, ebook_link, status, display_order, featured)
VALUES
    (
        'Bobo et ses copains remportent le tournoi de football',
        'bobo-et-ses-copains-remportent-le-tournoi-de-football',
        'Bobo et ses copains rejoignent l''équipe de Monsieur Ngabo, un entraîneur bienveillant et respecté.',
        'Bobo et ses copains rejoignent l''équipe de Monsieur Ngabo, un entraîneur bienveillant et respecté. Lorsque Tino, un talentueux attaquant, montre un manque d''engagement, Monsieur Ngabo le laisse sur le banc lors d''un match crucial, donnant à Bobo l''occasion de briller. Grâce à son courage, Bobo marque deux buts décisifs, menant l''équipe à la victoire. Prenant conscience de ses erreurs, Tino s''excuse et s''engage à changer, inspirant ses coéquipiers par son nouvel engagement. Ensemble, ils apprennent que le talent est important, mais que l''engagement, le courage et la solidarité sont essentiels pour réussir.',
        '/images/boboetsescopainsremportentletournoidefootball.jpg',
        '#',
        'https://crafty-speaker-8203.ck.page/products/besc-remportent-le-tournoi-de-foot',
        'PUBLISHED',
        1,
        true
    ),
    (
        'Bobo et ses cousins en vacances chez mamie',
        'bobo-et-ses-cousins-en-vacances-chez-mamie',
        'Bobo et ses cousins viennent d''arriver en vacances chez mamie.',
        'Bobo et ses cousins viennent d''arriver en vacances chez mamie. Ils aident un nouveau voisin de leur mamie à avoir des bonnes relations avec ses voisins.',
        '/images/book-bobosetsescousins.png',
        'https://www.amazon.fr/gp/product/B09Z9T7FB6',
        'https://crafty-speaker-8203.ck.page/products/bobo-et-ses-cousins-en-vacances',
        'PUBLISHED',
        2,
        true
    ),
    (
        'Bobo et ses copains jouent au football',
        'bobo-et-ses-copains-jouent-au-football',
        'Bobo et ses copains rêvent de faire partie d''une équipe de football.',
        'Bobo et ses copains rêvent de faire partie d''une équipe de football. Ils décident de fabriquer eux-mêmes leur ballon de football "KARERE". C''est le début d''une grande aventure qui commence.',
        '/images/book-bobojouefootball.png',
        'https://www.amazon.fr/Bobo-ses-copains-jouent-football/dp/B091WM9FVX',
        'https://crafty-speaker-8203.ck.page/products/bobo-et-ses-copains-jouent-au-foot',
        'PUBLISHED',
        3,
        true
    ),
    (
        'Bobo apprend à grimper aux arbres',
        'bobo-apprend-a-grimper-aux-arbres',
        'Bobo regarde tous les jours ses copains s''amuser à grimper aux arbres jusqu''à ce qu''un jour il décide d''apprendre à grimper aussi.',
        'Bobo regarde tous les jours ses copains s''amuser à grimper aux arbres jusqu''à ce qu''un jour il décide d''apprendre à grimper aussi. La première fois il tombe et il se fait mal à la cheville mais après sa guérison il recommence. Va-t-il réussir ?',
        '/images/book-boboapprendagremper.png',
        'https://www.amazon.fr/Bobo-apprend-grimper-aux-arbres/dp/B08NYGJY1B',
        'https://crafty-speaker-8203.ck.page/products/bobo-apprend-a-grimper-aux-arbres',
        'PUBLISHED',
        4,
        true
    );
