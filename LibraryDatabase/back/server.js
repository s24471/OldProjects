const e = require('express');
const express = require('express');
const app = express();
const port = 3000;
const cors = require('cors');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');

app.use(express.json());
app.use(cors({
    origin: 'http://localhost:4200' 
}));
app.listen(port, () => {
    console.log(`Server running on http://localhost:${port}`);
});

const JWT_SECRET = 'aleLubieTiny2023s24471';
const mysql = require('mysql');

const connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '1234',
    database: 'main'
});

connection.connect(error => {
    if (error) throw error;
    console.log("Successfully connected to the database.");
    
});



app.get('/api/categories', async(req, res)=>{
    
    const page = parseInt(req.query.page) || 1; 
    const limit = parseInt(req.query.limit) || 10; 
    const offset = (page - 1) * limit;

    console.log("prosba o kategorie");
    try{
        const query = `SELECT * from Kategorie`+((limit!=-1)?`
                    LIMIT ${limit} OFFSET ${offset}`:'');
                    
                    
                    const countQuery = `
                    SELECT COUNT(*) AS total
                    FROM Kategorie K`;
            
                    connection.query(query, function(bookErr, kategoriaResults) {
                        if (bookErr) {
                            console.error('Error fetching books: ' + bookErr.message);
                            res.status(500).send('Error fetching books' );
                        } else {
                            connection.query(countQuery, function(countErr, countResults) {
                                if (countErr) {
                                    console.error('Error counting books: ' + countErr.message);
                                    res.status(500).send('Error counting books');
                                } else {
                                    console.log(countResults[0].total);
                                    res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                                    res.status(200).json({ kategorie: kategoriaResults, total: countResults[0].total });
                                }
                            });
                        }
                    });
    } catch (error){
        console.error(error.message);
        res.status(500).send('Server error' + error.message);
    }
});
app.get('/api/books', async(req, res)=>{
    console.log("prosba o ksiazki");
    const page = parseInt(req.query.page) || 1; 
    const limit = parseInt(req.query.limit) || 10; 
    const offset = (page - 1) * limit;

    try{
        const query = `
        SELECT
            K.książkaID AS "ksiazkaID",
            K.tytuł AS "tytul",
            A.imię AS "imie",
            A.nazwisko AS "nazwisko",
            A.rokUrodzenia AS "rokUrodzenia",
            K.rokWydania AS "rokWydania",
            Ka.nazwaKategorii AS "nazwaKategorii"
        FROM
            Książki K
        INNER JOIN Autorzy A ON K.autorID = A.autorID
        INNER JOIN Kategorie Ka ON K.kategoriaID = Ka.kategoriaID
        LIMIT ${limit} OFFSET ${offset}`;
        const countQuery = `SELECT COUNT(*) AS total FROM Książki`;

        connection.query(query, function(bookErr, bookResults) {
            if (bookErr) {
                console.error('Error fetching books: ' + bookErr.message);
                res.status(500).send('Error fetching books');
            } else {
                connection.query(countQuery, function(countErr, countResults) {
                    if (countErr) {
                        console.error('Error counting books: ' + countErr.message);
                        res.status(500).send('Error counting books');
                    } else {
                        res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                        res.status(200).json({ books: bookResults, total: countResults[0].total });
                    }
                });
            }
        });
    } catch (error) {
        res.status(500).send('Server error');
    }
});


app.get('/api/bookID/:id', async(req, res)=>{
    console.log("prosba o ksiazki");
    const bookId = req.params.id;
    try{
        const query = `
        SELECT
            Ka.kategoriaID AS "kategoriaID",
            K.książkaID AS "ksiazkaID",
            A.autorID AS "autorID",
            K.tytuł AS "tytul",
            A.imię AS "imie",
            A.nazwisko AS "nazwisko",
            A.rokUrodzenia AS "rokUrodzenia",
            K.rokWydania AS "rokWydania",
            Ka.nazwaKategorii AS "nazwaKategorii"
        FROM
            Książki K
        INNER JOIN Autorzy A ON K.autorID = A.autorID
        INNER JOIN Kategorie Ka ON K.kategoriaID = Ka.kategoriaID
        WHERE K.książkaID = ${connection.escape(bookId)}`;
    
        connection.query(query, function(err, results){
            if (err){
                console.error('Błąd podczas wykonywania zapytania: ' + err.message);
            } else {
                res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                res.json(results[0]);
            }  
        });
    } catch (error) {
        res.status(500).send('Server error');
    }
});


app.get('/api/autors', async(req, res)=>{
    
    const page = parseInt(req.query.page) || 1; 
    const limit = parseInt(req.query.limit) || 10; 
    const offset = (page - 1) * limit;

    console.log("prosba o  autorow");
    try{
        const query = `
            SELECT 
                A.autorID as "autorID",
                A.imię as "imie",
                A.nazwisko as "nazwisko",
                A.rokUrodzenia as "rokUrodzenia"
                FROM
                    Autorzy A`+((limit!=-1)?`
                    LIMIT ${limit} OFFSET ${offset}`:'');
                    
                    
                    const countQuery = `
                    SELECT COUNT(*) AS total
                    FROM Autorzy A
                    `;
            
                    connection.query(query, function(bookErr, autorResults) {
                        if (bookErr) {
                            console.error('Error fetching books: ' + bookErr.message);
                            res.status(500).send('Error fetching books' );
                        } else {
                            connection.query(countQuery, function(countErr, countResults) {
                                if (countErr) {
                                    console.error('Error counting books: ' + countErr.message);
                                    res.status(500).send('Error counting books');
                                } else {
                                    res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                                    res.status(200).json({ autors: autorResults, total: countResults[0].total });
                                }
                            });
                        }
                    });
    } catch (error){
        res.status(500).send('Server error' + error.message);
    }
});

app.get('/api/autorID/:id', async(req, res)=>{
    console.log("prosba o  autorow");
    
    const autorID = req.params.id;
    try{
        const query = `
            SELECT 
                A.autorID as "autorID",
                A.imię as "imie",
                A.nazwisko as "nazwisko",
                A.rokUrodzenia as "rokUrodzenia"
                FROM
                    Autorzy A
                WHERE A.autorID =  ${connection.escape(autorID)}`;
        connection.query(query, function(err, results){
            if(err){
                console.error('Błąd : ' + err.message);
            }else{
                res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                res.json(results[0]);
            }
        });
    } catch (error){
        res.status(500).send('Server error');
    }
});

app.get('/api/users', authenticateToken, authenticateAdmin, async(req, res)=>{
    const page = parseInt(req.query.page) || 1; 
    const limit = parseInt(req.query.limit) || 10; 
    const offset = (page - 1) * limit;

    console.log("prosba o uzytkownikow");
    try{
        const query = `
            SELECT 
                U.użytkownikID as "uzytkownikID",
                U.login as "login",
                U.email as "email",
                U.imie as "imie",
                U.nazwisko as "nazwisko",
                U.admin as "admin"
                FROM
                    Użytkownicy U
                LIMIT ${limit} OFFSET ${offset}
        `;
        const countQuery = `
        SELECT COUNT(*) AS total
        FROM Użytkownicy U
        `;

        connection.query(query, function(bookErr, userResults) {
            if (bookErr) {
                console.error('Error fetching books: ' + bookErr.message);
                res.status(500).send('Error fetching books');
            } else {
                connection.query(countQuery, function(countErr, countResults) {
                    if (countErr) {
                        console.error('Error counting books: ' + countErr.message);
                        res.status(500).send('Error counting books');
                    } else {
                        res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                        res.status(200).json({ users: userResults, total: countResults[0].total });
                    }
                });
            }
        });
    } catch (error){
        res.status(500).send('Server error');
    }
});


app.get('/api/userID/:id', authenticateToken, authenticateAdmin, async(req, res)=>{
    console.log("prosba o uzytkownika");
    
    const userID = req.params.id;
    try{
        const query = `
            SELECT 
                U.użytkownikID as "uzytkownikID",
                U.login as "login",
                U.email as "email",
                U.imie as "imie",
                U.nazwisko as "nazwisko",
                U.admin as "admin"
                FROM
                    Użytkownicy U
                WHERE U.użytkownikID =  ${connection.escape(userID)}`;
        connection.query(query, function(err, results){
            if(err){
                console.error('Błąd : ' + err.message);
            }else{
                res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                res.json(results[0]);
            }
        });
    } catch (error){
        res.status(500).send('Server error');
    }
});


app.get('/api/categoryID/:id', async(req, res)=>{
    console.log("prosba o  kategorie");
    
    const kategoriaID = req.params.id;
    try{
        const query = `
            SELECT 
               *
                FROM
                    Kategorie K
                WHERE K.kategoriaID =  ${connection.escape(kategoriaID)}`;
        connection.query(query, function(err, results){
            if(err){
                console.error('Błąd : ' + err.message);
            }else{
                res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                res.json(results[0]);
            }
        });
    } catch (error){
        res.status(500).send('Server error');
    }
});

app.put('/api/bookID/:id', authenticateToken, authenticateAdmin, async (req, res) => {
    const bookId = req.params.id;
    const bookData = req.body;

    try {
        const query = `
            UPDATE Książki
            SET
                tytuł = ${connection.escape(bookData.tytul)},
                autorID = ${connection.escape(bookData.autorID)},
                kategoriaID = ${connection.escape(bookData.kategoriaID)},
                rokWydania = ${connection.escape(bookData.rokWydania)}
            WHERE książkaID = ${connection.escape(bookId)}
        `;

        connection.query(query, function(err, results) {
            if (err) {
                console.error('Błąd podczas aktualizacji książki: ' + err.message);
                res.status(500).send('Błąd podczas aktualizacji książki.');
            } else {
                const query = `
                    SELECT
                        Ka.kategoriaID AS "kategoriaID",
                        K.książkaID AS "ksiazkaID",
                        A.autorID AS "autorID",
                        K.tytuł AS "tytul",
                        A.imię AS "imie",
                        A.nazwisko AS "nazwisko",
                        A.rokUrodzenia AS "rokUrodzenia",
                        K.rokWydania AS "rokWydania",
                        Ka.nazwaKategorii AS "nazwaKategorii"
                    FROM
                        Książki K
                    INNER JOIN Autorzy A ON K.autorID = A.autorID
                    INNER JOIN Kategorie Ka ON K.kategoriaID = Ka.kategoriaID
                    WHERE K.książkaID = ${connection.escape(bookId)}`;
    
                connection.query(query, function(err, results){
                    if (err){
                        console.error('Błąd podczas wykonywania zapytania: ' + err.message);
                    } else {
                        res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                        res.json(results[0]);
                    }  
                });
            }
        });
    } catch (error) {
        console.error('Server error:', error);
        res.status(500).send('Server error');
    }
});

app.delete('/api/deleteBook/:id', authenticateToken, authenticateAdmin, async (req, res) => {
    const bookId = req.params.id;

    try {
        const query = `DELETE FROM Książki WHERE książkaID = ${connection.escape(bookId)}`;
        connection.query(query, function(err, results) {
            if (err) {
                console.error('Błąd podczas usuwania książki: ' + err.message);
                res.status(500).send('Błąd podczas usuwania książki.');
            } else {
                res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                res.status(200).send();
            }
        });
    } catch (error) {
        console.error('Server error:', error);
        res.status(500).send('Server error');
    }
});

app.post('/api/addBook', authenticateToken, authenticateAdmin, (req, res) => {
    const newBook = req.body;
    const query = `INSERT INTO Książki (tytuł, autorID, kategoriaID, rokWydania) VALUES (?, ?, ?, ?)`;
    connection.query(query, [newBook.tytul, newBook.autorID, newBook.kategoriaID, newBook.rokWydania], (err, results) => {
        if (err) {
            console.error('Błąd podczas dodawania książki: ' + err.message);
            res.status(500).send('Błąd podczas dodawania książki.');
        } else {
            res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
            res.status(201).send({ message: 'Książka została dodana pomyślnie.', bookId: results.insertId });
        }
    });
});





app.post('/api/addAutor', authenticateToken, authenticateAdmin, (req, res) => {
    const newAutor = req.body;
    const query = `INSERT INTO Autorzy (imię, nazwisko, rokUrodzenia) VALUES (?, ?, ?)`;
    connection.query(query, [newAutor.imie, newAutor.nazwisko, newAutor.rokUrodzenia], (err, results) => {
        if (err) {
            console.error('Błąd podczas dodawania autora: ' + err.message);
            res.status(500).send('Błąd podczas dodawania autora.');
        } else {
            res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
            res.status(201).send({ message: 'Autor został dodany pomyślnie.', autorId: results.insertId });
        }
    });
});

app.put('/api/autorID/:id', authenticateToken, authenticateAdmin, (req, res) => {
    const autorId = req.params.id;
    const autorData = req.body;

    const query = `UPDATE Autorzy SET imię = ?, nazwisko = ?, rokUrodzenia = ? WHERE autorID = ?`;
    connection.query(query, [autorData.imie, autorData.nazwisko, autorData.rokUrodzenia, autorId], (err, results) => {
        if (err) {
            console.error('Błąd podczas aktualizacji autora: ' + err.message);
            res.status(500).send('Błąd podczas aktualizacji autora.');
        } else {
        const query = `
            SELECT 
                A.autorID as "autorID",
                A.imię as "imie",
                A.nazwisko as "nazwisko",
                A.rokUrodzenia as "rokUrodzenia"
                FROM
                    Autorzy A
                WHERE A.autorID =  ${connection.escape(autorId)}`;
        connection.query(query, function(err, results){
            if(err){
                console.error('Błąd : ' + err.message);
            }else{
                res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                res.json(results[0]);
            }
        });
        }
    });
});

app.delete('/api/deleteAutor/:id', authenticateToken, authenticateAdmin, (req, res) => {
    const autorId = req.params.id;

    connection.beginTransaction(err => {
        if (err) { throw err; }
        connection.query('DELETE FROM Książki WHERE autorID = ?', [autorId], (err, results) => {
            if (err) {
                return connection.rollback(() => {
                    throw err;
                });
            }

            connection.query('DELETE FROM Autorzy WHERE autorID = ?', [autorId], (err, results) => {
                if (err) {
                    return connection.rollback(() => {
                        throw err;
                    });
                }

                connection.commit(err => {
                    if (err) {
                        return connection.rollback(() => {
                            throw err;
                        });
                    }
                    res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                    res.status(200).send({ message: 'Autor i jego książki zostały usunięte pomyślnie.' });
                });
            });
        });
    });
});

app.post('/api/addCategory', authenticateToken, authenticateAdmin, (req, res) => {
    const newCategory = req.body;
    const query = `INSERT INTO Kategorie (nazwaKategorii, rokUtworzenia) VALUES (?, ?)`;
    connection.query(query, [newCategory.nazwaKategorii, newCategory.rokUtworzenia], (err, results) => {
        if (err) {
            console.error('Błąd podczas dodawania kategorii: ' + err.message);
            res.status(500).send('Błąd podczas dodawania kategorii.');
        } else {
            res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
            res.status(201).send({ message: 'Kategoria została dodana pomyślnie.', categoryId: results.insertId });
        }
    });
});

app.put('/api/categoryID/:id', authenticateToken, authenticateAdmin, (req, res) => {
    const categoryId = req.params.id;
    const categoryData = req.body;

    const query = `UPDATE Kategorie SET nazwaKategorii = ?, rokUtworzenia = ? WHERE kategoriaID = ?`;
    connection.query(query, [categoryData.nazwaKategorii,categoryData.rokUtworzenia, categoryId], (err, results) => {
        if (err) {
            console.error('Błąd podczas aktualizacji kategorii: ' + err.message);
            res.status(500).send('Błąd podczas aktualizacji kategorii.');
        } else {
            const query = `
            SELECT 
               *
                FROM
                    Kategorie K
                WHERE K.kategoriaID =  ${connection.escape(categoryId)}`;
        connection.query(query, function(err, results){
            if(err){
                console.error('Błąd : ' + err.message);
            }else{
                res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                res.json(results[0]);
            }
        });
        }
    });
});

app.delete('/api/deleteCategory/:id', authenticateToken, authenticateAdmin, (req, res) => {
    const categoryId = req.params.id;

    connection.beginTransaction(err => {
        if (err) { throw err; }

        connection.query('DELETE FROM Książki WHERE kategoriaID = ?', [categoryId], (err, results) => {
            if (err) {
                return connection.rollback(() => {
                    console.error('Błąd podczas usuwania książek: ' + err.message);
                    res.status(500).send('Błąd podczas usuwania książek.');
                });
            }

            connection.query('DELETE FROM Kategorie WHERE kategoriaID = ?', [categoryId], (err, results) => {
                if (err) {
                    return connection.rollback(() => {
                        console.error('Błąd podczas usuwania kategorii: ' + err.message);
                        res.status(500).send('Błąd podczas usuwania kategorii.');
                    });
                }

                connection.commit(err => {
                    if (err) {
                        return connection.rollback(() => {
                            throw err;
                        });
                    }
                    res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                    res.status(200).send({ message: 'Kategoria i wszystkie powiązane książki zostały usunięte pomyślnie.' });
                });
            });
        });
    });
});




app.put('/api/userID/:id', authenticateToken, authenticateAdmin, (req, res) => {
    const userId = req.params.id;
    const userData = req.body;

    const query = `UPDATE Użytkownicy SET login = ?, email = ?, imie = ?, nazwisko = ?, admin = ? WHERE użytkownikID = ?`;

    console.log("prosba o zaaktualizowanie uzytkownika");
    connection.query(query, [userData.login, userData.email, userData.imie, userData.nazwisko, userData.admin, userId], (err, results) => {
        if (err) {
            console.error('Błąd podczas aktualizacji użytkownika: ' + err.message);
            res.status(500).send('Błąd podczas aktualizacji użytkownika.');
        } else {
            res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
            console.log("uzytkownik zostal zaaktualizowany");
            const query = `
                SELECT 
                    U.użytkownikID as "uzytkownikID",
                    U.login as "login",
                    U.email as "email",
                    U.imie as "imie",
                    U.nazwisko as "nazwisko",
                    U.admin as "admin"
                FROM
                        Użytkownicy U
                WHERE U.użytkownikID =  ${connection.escape(userId)}`;
            connection.query(query, function(err, results){
            if(err){
                console.error('Błąd : ' + err.message);
            }else{
                res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                res.json(results[0]);
            }
            });
        }
    });
});

app.delete('/api/deleteUser/:id', authenticateToken, authenticateAdmin, (req, res) => {
    const userId = req.params.id;

    const query = `DELETE FROM Użytkownicy WHERE użytkownikID = ?`;

    connection.query(query, [userId], (err, results) => {
        if (err) {
            console.error('Błąd podczas usuwania użytkownika: ' + err.message);
            res.status(500).send('Błąd podczas usuwania użytkownika.');
        } else {
            res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
            res.status(200).send({ message: 'Użytkownik został usunięty pomyślnie.' });
        }
    });
});


app.get('/api/booksByAutor/:autorId', async (req, res) => {
    const autorId = req.params.autorId;
    const page = parseInt(req.query.page) || 1; 
    const limit = parseInt(req.query.limit) || 10; 
    const offset = (page - 1) * limit;

    try {
        const query = `
            SELECT
                Ka.kategoriaID AS "kategoriaID",
                K.książkaID AS "ksiazkaID",
                A.autorID AS "autorID",
                K.tytuł AS "tytul",
                A.imię AS "imie",
                A.nazwisko AS "nazwisko",
                A.rokUrodzenia AS "rokUrodzenia",
                K.rokWydania AS "rokWydania",
                Ka.nazwaKategorii AS "nazwaKategorii"
            FROM
                Książki K
            INNER JOIN Autorzy A ON K.autorID = A.autorID
            INNER JOIN Kategorie Ka ON K.kategoriaID = Ka.kategoriaID
            WHERE K.autorID = ${connection.escape(autorId)}
            LIMIT ${limit} OFFSET ${offset}
            `;

            const countQuery = `
            SELECT COUNT(*) AS total
            FROM Książki K
            WHERE K.autorID = ${connection.escape(autorId)}
            `;
    
            connection.query(query, function(bookErr, bookResults) {
                if (bookErr) {
                    console.error('Error fetching books: ' + bookErr.message);
                    res.status(500).send('Error fetching books');
                } else {
                    connection.query(countQuery, function(countErr, countResults) {
                        if (countErr) {
                            console.error('Error counting books: ' + countErr.message);
                            res.status(500).send('Error counting books');
                        } else {
                            res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                            res.status(200).json({ books: bookResults, total: countResults[0].total });
                        }
                    });
                }
            });
    } catch (error) {
        console.error('Server error:', error);
        res.status(500).send('Server error');
    }
});



app.get('/api/booksByCategory/:categoryId', async (req, res) => {
    const categoryId = req.params.categoryId;
    const page = parseInt(req.query.page) || 1; 
    const limit = parseInt(req.query.limit) || 10; 
    const offset = (page - 1) * limit;

    try {
        const query = `
            SELECT
                Ka.kategoriaID AS "kategoriaID",
                K.książkaID AS "ksiazkaID",
                A.autorID AS "autorID",
                K.tytuł AS "tytul",
                A.imię AS "imie",
                A.nazwisko AS "nazwisko",
                A.rokUrodzenia AS "rokUrodzenia",
                K.rokWydania AS "rokWydania",
                Ka.nazwaKategorii AS "nazwaKategorii"
            FROM
                Książki K
            INNER JOIN Autorzy A ON K.autorID = A.autorID
            INNER JOIN Kategorie Ka ON K.kategoriaID = Ka.kategoriaID
            WHERE K.kategoriaID = ${connection.escape(categoryId)}
            LIMIT ${limit} OFFSET ${offset}
            `;

        const countQuery = `
        SELECT COUNT(*) AS total
        FROM Książki K
        WHERE K.kategoriaID = ${connection.escape(categoryId)}
        `;

        connection.query(query, function(bookErr, bookResults) {
            if (bookErr) {
                console.error('Error fetching books: ' + bookErr.message);
                res.status(500).send('Error fetching books');
            } else {
                connection.query(countQuery, function(countErr, countResults) {
                    if (countErr) {
                        console.error('Error counting books: ' + countErr.message);
                        res.status(500).send('Error counting books');
                    } else {
                        console.log(countResults[0].total);
                        res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                        res.status(200).json({ books: bookResults, total: countResults[0].total });
                    }
                });
            }
        });
    } catch (error) {
        console.error('Server error:', error);
        res.status(500).send('Server error');
    }
});

app.post('/api/register', async (req, res) => {
    const newUser = req.body;
    console.log(newUser);
    if(!newUser.haslo) return res.status(500).send('Błąd podczas dodawania użytkownika.');
    if(newUser.haslo.length<5)
        return res.status(500).send("HASŁO MUSI BYC >=5");
    const hashedPassword = await bcrypt.hash(newUser.haslo, 10); 
    const query = `INSERT INTO Użytkownicy (login, hasło, email, imie, nazwisko, admin) VALUES (?, ?, ?, ?, ?, ?)`;

    connection.query(query, [newUser.login, hashedPassword, newUser.email, newUser.imie, newUser.nazwisko, false], (err, results) => {
        if (err) {
            console.error('Błąd podczas dodawania użytkownika: ' + err.message);
            res.status(500).send('Błąd podczas dodawania użytkownika.');
        } else {
            res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
            res.status(201).send({ message: 'Użytkownik został dodany pomyślnie.', userId: results.insertId });
        }
    });
});


app.post('/api/login', async (req, res) => {
    const { login, password } = req.body;
    
    connection.query('SELECT * FROM Użytkownicy WHERE login = ?', [login], function(err, results) {
        if (err) {
            return res.status(500).send('Błąd serwera');
        }

        if (results.length === 0) {
            return res.status(401).send('Nieprawidłowy login lub hasło');
        }

        const user = results[0];
       
        if (!bcrypt.compareSync(password, user.hasło)) {
            return res.status(401).send('Nieprawidłowy login lub hasło');
        }
        const token = jwt.sign({ 
            userId: user.użytkownikID, 
            login: user.login, 
            isAdmin: user.admin 
        }, JWT_SECRET, { expiresIn: '1h' });
        
        res.json({ token, isAdmin: user.admin });
    });
});


function authenticateToken(req, res, next) {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (token == null) {
        console.log('brak tokenu');
        return res.sendStatus(401);
    }
    jwt.verify(token, JWT_SECRET, (err, user) => {
        if (err) {
            console.log('odrzucono uzytkownika: ');
            console.log(user);
            return res.sendStatus(403);
        }
        
        req.user = user;
        next();
    });
}

function authenticateAdmin(req, res, next) {
    if (!req.user || !req.user.isAdmin) {
        console.log('odrzucono admina: ');
        console.log(user);
        return res.status(403).send('Brak uprawnień administratora');
    }
    next();
}


app.post('/api/addToFavorites/:id', authenticateToken, (req, res) => {
    const userId = req.user.userId; 
    const ksiazkaID = req.params.id;

    const checkQuery = 'SELECT * FROM listy_książki WHERE Użytkownicy_użytkownikID = ? AND książkaID = ?';
    connection.query(checkQuery, [userId, ksiazkaID], (checkErr, checkResults) => {
        if (checkErr) {
            return res.status(500).send('Błąd serwera przy sprawdzaniu ulubionych');
        }

        if (checkResults.length > 0) {
            return res.status(409).send('Ta książka jest już w ulubionych');
        }

        const insertQuery = 'INSERT INTO listy_książki (Użytkownicy_użytkownikID, książkaID) VALUES (?, ?)';
        connection.query(insertQuery, [userId, ksiazkaID], (insertErr, insertResults) => {
            if (insertErr) {
                return res.status(500).send('Błąd serwera przy dodawaniu do ulubionych');
            }

            res.status(201).send();
        });
    });
});


app.get('/api/favorites', authenticateToken, (req, res) => {
    const userId = req.user.userId;
    const page = parseInt(req.query.page) || 1; 
    const limit = parseInt(req.query.limit) || 10; 
    const offset = (page - 1) * limit;

    const query = `
        SELECT
                Ka.kategoriaID AS "kategoriaID",
                K.książkaID AS "ksiazkaID",
                A.autorID AS "autorID",
                K.tytuł AS "tytul",
                A.imię AS "imie",
                A.nazwisko AS "nazwisko",
                A.rokUrodzenia AS "rokUrodzenia",
                K.rokWydania AS "rokWydania",
                Ka.nazwaKategorii AS "nazwaKategorii"
            FROM
                Książki K
            INNER JOIN Autorzy A ON K.autorID = A.autorID
            INNER JOIN Kategorie Ka ON K.kategoriaID = Ka.kategoriaID
            INNER JOIN listy_książki l ON K.książkaID = l.książkaID
            WHERE l.Użytkownicy_użytkownikID = ${userId}
            LIMIT ${limit} OFFSET ${offset}
        `;

        const countQuery = `
        SELECT COUNT(*) AS total
        FROM Książki K
        INNER JOIN listy_książki l ON K.książkaID = l.książkaID
        WHERE l.Użytkownicy_użytkownikID = ${userId}
        `;

        connection.query(query, function(bookErr, bookResults) {
            if (bookErr) {
                console.error('Error fetching books: ' + bookErr.message);
                res.status(500).send('Error fetching books');
            } else {
                connection.query(countQuery, function(countErr, countResults) {
                    if (countErr) {
                        console.error('Error counting books: ' + countErr.message);
                        res.status(500).send('Error counting books');
                    } else {
                        res.set('Access-Control-Allow-Origin', 'http://localhost:4200');
                        res.status(200).json({ books: bookResults, total: countResults[0].total });
                    }
                });
            }
        });
});

app.get('/api/isFavorite/:bookId', authenticateToken, (req, res) => {
    const userId = req.user.userId;
    const bookId = req.params.bookId;

    const query = 'SELECT * FROM listy_książki WHERE Użytkownicy_użytkownikID = ? AND książkaID = ?';
    connection.query(query, [userId, bookId], (err, results) => {
        if (err) {
            return res.status(500).send('Błąd serwera');
        }

        const isFavorite = results.length > 0;
        res.json({ isFavorite });
    });
});

app.delete('/api/removeFavorite/:bookId', authenticateToken, (req, res) => {
    const userId = req.user.userId; 
    const bookId = req.params.bookId;

    const query = 'DELETE FROM listy_książki WHERE Użytkownicy_użytkownikID = ? AND książkaID = ?';
    connection.query(query, [userId, bookId], (err, results) => {
        if (err) {
            console.error('Błąd podczas usuwania z ulubionych: ' + err.message);
            return res.status(500).send('Błąd serwera');
        }

        if (results.affectedRows === 0) {
            return res.status(404).send('Książka nie znaleziona w ulubionych');
        }

        res.status(200).send();
    });
});
