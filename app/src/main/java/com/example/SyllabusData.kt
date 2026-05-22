package com.example

object SyllabusData {
    val chapters: List<Chapter> = listOf(
        // --- FORM 4 ---
        Chapter(
            number = 1,
            form = 4,
            name = TranslatedText("Functions", "Fungsi"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Function Definition", "Definisi Fungsi"),
                    listOf(
                        TranslatedText("A function maps each element in domain to exactly one image in codomain.", "Satu fungsi memetakan setiap unsur dalam domain kepada hanya satu imej dalam kodomain."),
                        TranslatedText("Vertical Line Test: If any vertical line intersects a graph at most once, it is a function.", "Ujian Garis Tegak: Jika sebarang garis tegak memotong graf paling banyak sekali, ia ialah fungsi.")
                    )
                ),
                NoteSection(
                    TranslatedText("Composite and Inverse Functions", "Fungsi Gubahan & Songsang"),
                    listOf(
                        TranslatedText("Composite function fg(x) means finding g(x) first, then substituting it into f(x).", "Fungsi gubahan fg(x) bermaksud mencari g(x) dahulu, kemudian menggantikannya ke dalam f(x)."),
                        TranslatedText("Only one-to-one functions have inverse functions. Subtest with Horizontal Line Test.", "Hanya fungsi satu-ke-satu mempunyai fungsi songsang. Uji menggunakan Ujian Garis Mengufuk.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Absolute Value Function", "Fungsi Nilai Mutlak"),
                    "|f(x)| = f(x) if f(x) >= 0; otherwise -f(x)",
                    TranslatedText("Output is always non-negative.", "Output sentiasa bukan negatif.")
                ),
                Formula(
                    TranslatedText("Inverse Function Identity", "Identiti Fungsi Songsang"),
                    "f(f^-1(x)) = x",
                    TranslatedText("An inverse function reverses the mapping of a function.", "Fungsi songsang menyongsangkan pemetaan suatu fungsi.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f4_c1_p1", Difficulty.EASY,
                    TranslatedText("Given f(x) = 3x + 1, find f(2).", "Diberi f(x) = 3x + 1, cari f(2)."),
                    "7",
                    TranslatedText("Substitute x = 2 into f(x).", "Gantikan x = 2 ke dalam f(x)."),
                    TranslatedText("f(2) = 3(2) + 1 = 6 + 1 = 7.", "f(2) = 3(2) + 1 = 6 + 1 = 7.")
                ),
                PracticeQuestion(
                    "f4_c1_p2", Difficulty.MEDIUM,
                    TranslatedText("Given f(x) = 2x - 3, find the inverse function f^-1(x).", "Diberi f(x) = 2x - 3, cari fungsi songsang f^-1(x)."),
                    "x+3/2",
                    TranslatedText("Let y = f(x), then make x the subject of the formula.", "Biarkan y = f(x), kemudian jadikan x sebagai tajuk rumus."),
                    TranslatedText("Let y = 2x - 3 => y + 3 = 2x => x = (y+3)/2. Thus, f^-1(x) = (x+3)/2.", "Biar y = 2x - 3 => y + 3 = 2x => x = (y+3)/2. Maka, f^-1(x) = (x+3)/2.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f4_c1_q1",
                    TranslatedText("If f(x) = 3x - 5 and g(x) = x^2, find the composite function fg(x).", "Jika f(x) = 3x - 5 dan g(x) = x^2, cari fungsi gubahan fg(x)."),
                    listOf("3x^2 - 5", "(3x - 5)^2", "3x^2 - 15", "x^2(3x - 5)"),
                    0,
                    TranslatedText("fg(x) = f(g(x)) = f(x^2) = 3(x^2) - 5 = 3x^2 - 5.", "fg(x) = f(g(x)) = f(x^2) = 3(x^2) - 5 = 3x^2 - 5.")
                )
            )
        ),
        Chapter(
            number = 2,
            form = 4,
            name = TranslatedText("Quadratic Functions", "Fungsi Kuadratik"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Quadratic Equations & Roots", "Persamaan Kuadratik & Punca"),
                    listOf(
                        TranslatedText("General form: ax^2 + bx + c = 0.", "Bentuk am: ax^2 + bx + c = 0."),
                        TranslatedText("Sum of roots (SOR) = -b/a. Product of roots (POR) = c/a.", "Hasil tambah punca (HTP) = -b/a. Hasil darab punca (HDP) = c/a.")
                    )
                ),
                NoteSection(
                    TranslatedText("Discriminant and Root Types", "Pembezalayan dan Jenis Punca"),
                    listOf(
                        TranslatedText("Discriminant (b^2 - 4ac) determines the type of roots.", "Pembezalayan (b^2 - 4ac) menentukan jenis punca."),
                        TranslatedText("b^2 - 4ac >  0: Two different real roots.", "b^2 - 4ac > 0: Dua punca nyata yang berbeza."),
                        TranslatedText("b^2 - 4ac =  0: Two equal real roots.", "b^2 - 4ac = 0: Dua punca nyata yang sama."),
                        TranslatedText("b^2 - 4ac <  0: No real roots.", "b^2 - 4ac < 0: Tiada punca nyata.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Quadratic Formula", "Rumus Kuadratik"),
                    "x = [-b +- sqrt(b^2 - 4ac)] / (2a)",
                    TranslatedText("Used to solve for roots of quadratic equations directly.", "Digunakan untuk mencari punca persamaan kuadratik secara terus.")
                ),
                Formula(
                    TranslatedText("Vertex Form", "Bentuk Verteks"),
                    "f(x) = a(x - h)^2 + k",
                    TranslatedText("In vertex form, (h, k) is the maximum or minimum point coordinate.", "Dalam bentuk verteks, (h, k) adalah koordinat titik maksimum atau minimum.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f4_c2_p1", Difficulty.MEDIUM,
                    TranslatedText("Find the value of discriminant for 2x^2 + 5x + 3 = 0.", "Cari nilai pembezalayan bagi 2x^2 + 5x + 3 = 0."),
                    "1",
                    TranslatedText("Use b^2 - 4ac where a=2, b=5, c=3.", "Gunakan b^2 - 4ac dengan a=2, b=5, c=3."),
                    TranslatedText("b^2 - 4ac = 5^2 - 4(2)(3) = 25 - 24 = 1. (Two different real roots).", "b^2 - 4ac = 5^2 - 4(2)(3) = 25 - 24 = 1. (Dua punca nyata berbeza).")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f4_c2_q1",
                    TranslatedText("State the condition of roots if b^2 - 4ac < 0.", "Nyatakan keadaan punca-punca jika b^2 - 4ac < 0."),
                    listOf("Two different real roots", "Two equal real roots", "No real roots", "Complex integer roots"),
                    2,
                    TranslatedText("When discriminat is less than zero, the graph does not cross the x-axis, meaning no real roots exist.", "Apabila pembezalayan kurang daripada sifar, graf tidak memotong paksi-x, bermakna tiada punca nyata wujud.")
                )
            )
        ),
        Chapter(
            number = 3,
            form = 4,
            name = TranslatedText("Systems of Equations", "Sistem Persamaan"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Three Variable Systems", "Sistem Tiga Pemboleh Ubah"),
                    listOf(
                        TranslatedText("A system of linear equations in three variables consists of equations with variables (usually x, y, z).", "Sistem persamaan linear dalam tiga pemboleh ubah terdiri daripada persamaan dengan pemboleh ubah (biasanya x, y, z)."),
                        TranslatedText("Solve using the elimination or substitution method.", "Selesaikan menggunakan kaedah penghapusan atau penggantian.")
                    )
                ),
                NoteSection(
                    TranslatedText("Linear and Non-Linear Equations", "Persamaan Linear dan Bukan Linear"),
                    listOf(
                        TranslatedText("Consists of one linear equation (degree 1) and one non-linear equation (degree 2).", "Terdiri daripada satu persamaan linear (darjah 1) dan satu persamaan bukan linear (darjah 2)."),
                        TranslatedText("Always substitute the linear equation into the non-linear equation to solve.", "Sentiasa gantikan persamaan linear ke dalam persamaan bukan linear untuk menyelesaikan.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Linear Equation Form", "Bentuk Persamaan Linear"),
                    "Ax + By + Cz = D",
                    TranslatedText("Represents a flat plane in 3D coordinate space.", "Mewakili satah rata dalam ruang koordinat 3D.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f4_c3_p1", Difficulty.MEDIUM,
                    TranslatedText("Solve for x and y: y - x = 2 and x^2 + y^2 = 10. Give the positive coordinates.", "Selesaikan x dan y: y - x = 2 dan x^2 + y^2 = 10. Berikan koordinat positif."),
                    "x=1, y=3",
                    TranslatedText("Substitute y = x + 2 into the second equation.", "Gantikan y = x + 2 ke dalam persamaan kedua."),
                    TranslatedText("x^2 + (x+2)^2 = 10 => 2x^2 + 4x + 4 = 10 => 2x^2 + 4x - 6 = 0 => x^2 + 2x - 3 = 0 => (x+3)(x-1) = 0. For positive values, x = 1, y = 3.", "x^2 + (x+2)^2 = 10 => 2x^2 + 4x + 4 = 10 => 2x^2 + 4x - 6 = 0 => x^2 + 2x - 3 = 0 => (x+3)(x-1) = 0. Bagi nilai positif, x = 1, y = 3.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f4_c3_q1",
                    TranslatedText("Which method is best for starting simultaneous equations of one linear and one non-linear?", "Kaedah manakah paling baik untuk memulakan persamaan serentak satu linear dan satu bukan linear?"),
                    listOf("Substitution method", "Matrix inversion", "Graphical sketching", "Elimination alone"),
                    0,
                    TranslatedText("The substitution method is cleanest because you can express one variable in terms of another from the linear equation.", "Kaedah penggantian adalah paling bersih kerana anda boleh menyatakan satu pemboleh ubah dalam sebutan pemboleh ubah lain daripada persamaan linear.")
                )
            )
        ),
        Chapter(
            number = 4,
            form = 4,
            name = TranslatedText("Indices, Surds and Logarithms", "Indeks, Surd dan Logaritma"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Laws of Indices & Logs", "Hukum Indeks & Logaritma"),
                    listOf(
                        TranslatedText("Multiplication of indices: a^m * a^n = a^(m+n).", "Pendaraban indeks: a^m * a^n = a^(m+n)."),
                        TranslatedText("Product law of logs: log_a(xy) = log_a(x) + log_a(y).", "Hukum pendaraban logaritma: log_a(xy) = log_a(x) + log_a(y)."),
                        TranslatedText("Dividing indices: a^m / a^n = a^(m-n).", "Pembahagian indeks: a^m / a^n = a^(m-n).")
                    )
                ),
                NoteSection(
                    TranslatedText("Surds Rationalization", "Nisbahkan Surd"),
                    listOf(
                        TranslatedText("Multiply both numerator and denominator by the conjugate surd to remove the surd in denominator.", "Darabkan kedua-dua pengangka dan penyebut dengan surd konjugat untuk menghapuskan surd di penyebut."),
                        TranslatedText("Conjugate of (a + sqrt(b)) is (a - sqrt(b)).", "Konjugat bagi (a + sqrt(b)) ialah (a - sqrt(b)).")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Change of Base of Log", "Penukaran Asas Logaritma"),
                    "log_b(a) = log_c(a) / log_c(b)",
                    TranslatedText("Useful for simplifying logs with different bases.", "Berguna untuk memudahkan logaritma dengan asas yang berbeza.")
                ),
                Formula(
                    TranslatedText("Log to Index Conversion", "Penukaran Log ke Indeks"),
                    "N = a^x <=> x = log_a(N)",
                    TranslatedText("Fundamental relation between power indices and logarithms.", "Hubungan asas antara indeks kuasa dan logaritma.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f4_c4_p1", Difficulty.EASY,
                    TranslatedText("Simplify: log_2(8).", "Permudahkan: log_2(8)."),
                    "3",
                    TranslatedText("Express 8 as a power of base 2.", "Nyatakan 8 sebagai kuasa asas 2."),
                    TranslatedText("log_2(8) = log_2(2^3) = 3 * log_2(2) = 3(1) = 3.", "log_2(8) = log_2(2^3) = 3 * log_2(2) = 3(1) = 3.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f4_c4_q1",
                    TranslatedText("What is the simplified form of log_a(x) - log_a(y)?", "Apakah bentuk pemudahan bagi log_a(x) - log_a(y)?"),
                    listOf("log_a(x/y)", "log_a(x - y)", "log_a(xy)", "log_a(x) / log_a(y)"),
                    0,
                    TranslatedText("According to the division law of logarithms, the subtraction of logs corresponds to the logarithm of the quotient.", "Mengikut hukum pembahagian logaritma, penolakan logaritma sepadan dengan logaritma bagi hasil bahagi.")
                )
            )
        ),
        Chapter(
            number = 5,
            form = 4,
            name = TranslatedText("Progressions", "Janjiang"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Arithmetic Progression (AP)", "Janjiang Aritmetik (JA)"),
                    listOf(
                        TranslatedText("A sequence with a constant common difference, d = T_n - T_(n-1).", "Suatu jujukan dengan beza sepunya pemalar, d = T_n - T_(n-1)."),
                        TranslatedText("S_n formula computes the sum of the first n terms.", "Rumus S_n mengira hasil tambah n sebutan pertama.")
                    )
                ),
                NoteSection(
                    TranslatedText("Geometric Progression (GP)", "Janjiang Geometri (JG)"),
                    listOf(
                        TranslatedText("A sequence with a constant common ratio, r = T_n / T_(n-1).", "Suatu jujukan dengan nisbah sepunya pemalar, r = T_n / T_(n-1)."),
                        TranslatedText("Sum to infinity exists only if |r| < 1.", "Hasil tambah hingga ketakterhinggaan wujud hanya jika |r| < 1.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("n-th term of AP", "Sebutan ke-n bagi JA"),
                    "T_n = a + (n - 1)d",
                    TranslatedText("a is the first term, d is the common difference.", "a ialah sebutan pertama, d ialah beza sepunya.")
                ),
                Formula(
                    TranslatedText("Sum to Infinity of GP", "Hasil Tambah hingga Ketakterhinggaan JG"),
                    "S_infinity = a / (1 - r)",
                    TranslatedText("Applicable only when -1 < r < 1.", "Boleh digunakan hanya apabila -1 < r < 1.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f4_c5_p1", Difficulty.MEDIUM,
                    TranslatedText("Find the 10th term of the AP: 3, 7, 11, ...", "Cari sebutan ke-10 bagi JA: 3, 7, 11, ..."),
                    "39",
                    TranslatedText("Identify a = 3, d = 4, then use T_n formula.", "Kenal pasti a = 3, d = 4, kemudian gunakan rumus T_n."),
                    TranslatedText("T_10 = 3 + (10 - 1)(4) = 3 + 9(4) = 3 + 36 = 39.", "T_10 = 3 + (10 - 1)(4) = 3 + 9(4) = 3 + 36 = 39.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f4_c5_q1",
                    TranslatedText("For a GP with first term a = 6, r = 0.5, find its sum to infinity.", "Bagi satu JG dengan sebutan pertama a = 6, r = 0.5, cari hasil tambah hingga ketakterhinggaannya."),
                    listOf("12", "18", "9", "24"),
                    0,
                    TranslatedText("S_inf = a / (1 - r) = 6 / (1 - 0.5) = 6 / 0.5 = 12.", "S_inf = a / (1 - r) = 6 / (1 - 0.5) = 6 / 0.5 = 12.")
                )
            )
        ),
        Chapter(
            number = 6,
            form = 4,
            name = TranslatedText("Linear Law", "Hukum Linear"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Converting Non-Linear to Linear", "Menukarkan Bukan Linear kepada Linear"),
                    listOf(
                        TranslatedText("Transform non-linear equations to Y = mX + C form to represent them as straight lines on a graph.", "Tukarkan persamaan bukan linear kepada bentuk Y = mX + C untuk mewakilinya sebagai garis lurus pada graf."),
                        TranslatedText("Plot Y against X where Y and X are composite variables of x and y.", "Plor Y melawan X di mana Y dan X ialah pemboleh ubah gubahan bagi x dan y.")
                    )
                ),
                NoteSection(
                    TranslatedText("Line of Best Fit", "Garis Penyuaian Terbaik"),
                    listOf(
                        TranslatedText("A line drawn through scatter points such that it represents the trend with minimum distance to overall points.", "Garis yang dilukis melalui titik-titik taburan supaya ia mewakili trend dengan jarak minimum ke titik-titik keseluruhan."),
                        TranslatedText("Points must be balanced evenly above and below the line.", "Titik-titik mesti seimbang di atas dan di bawah garis.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Linear Form Equation", "Persamaan Bentuk Linear"),
                    "Y = mX + C",
                    TranslatedText("m is gradient (kecerunan), C is Y-intercept (pintasan-Y).", "m ialah kecerunan, C ialah pintasan-Y.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f4_c6_p1", Difficulty.MEDIUM,
                    TranslatedText("Convert y = ab^x into linear form Y = mX + C.", "Tukarkan y = ab^x kepada bentuk linear Y = mX + C."),
                    "log y = log b * x + log a",
                    TranslatedText("Take common logarithm (base 10) on both sides.", "Ambil logaritma biasa (asas 10) pada kedua-dua belah."),
                    TranslatedText("log_10(y) = log_10(ab^x) => log_10(y) = (log_10(b)) * x + log_10(a). This matches Y = mX + C where Y = log_10(y), X = x.", "log_10(y) = log_10(ab^x) => log_10(y) = (log_10(b)) * x + log_10(a). Ini sepadan dengan Y = mX + C dengan Y = log_10(y), X = x.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f4_c6_q1",
                    TranslatedText("If we plot y/x against x^2 for the equation y = ax^3 + bx, what refers to the gradient?", "Jika kita memplot y/x melawan x^2 bagi persamaan y = ax^3 + bx, apakah yang merujuk kepada kecerunan?"),
                    listOf("a", "b", "ax", "y"),
                    0,
                    TranslatedText("Divide by x: y/x = ax^2 + b. Comparing to Y = mX + C, Y = y/x, X = x^2, so Gradient m = a, and Intercept C = b.", "Bahagi dengan x: y/x = ax^2 + b. Membandingkan dengan Y = mX + C, Y = y/x, X = x^2, maka Kecerunan m = a, dan Pintasan C = b.")
                )
            )
        ),
        Chapter(
            number = 7,
            form = 4,
            name = TranslatedText("Coordinate Geometry", "Geometri Koordinat"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Divisor of Line Segment", "Pembahagi Tembereng Garis"),
                    listOf(
                        TranslatedText("A point P dividing line segment AB in ratio m:n.", "Satu titik P yang membahagikan tembereng garis AB dengan nisbah m:n."),
                        TranslatedText("Midpoint is a special case where m = n = 1.", "Titik tengah ialah kes khas di mana m = n = 1.")
                    )
                ),
                NoteSection(
                    TranslatedText("Area of Polygons & Loci", "Luas Poligon & Lokus"),
                    listOf(
                        TranslatedText("Use the Shoelace Algorithm to find areas of triangles/quadrilaterals from coordinate points.", "Gunakan Algoritma Kasut (Shoelace) untuk mencari luas segi tiga/segi empat daripada titik koordinat."),
                        TranslatedText("Locus is the path traced by a moving point under specific mathematical constraints.", "Lokus ialah laluan yang dilalui oleh titik bergerak di bawah kekangan matematik tertentu.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Divisor Point P Formula", "Rumus Titik Pembahagi P"),
                    "((nx_1 + mx_2)/(m+n), (ny_1 + my_2)/(m+n))",
                    TranslatedText("Coordinates of point P dividing AB in ratio m:n.", "Koordinat titik P membahagi AB dalam nisbah m:n.")
                ),
                Formula(
                    TranslatedText("Locus Circle Equation", "Persamaan Lokus Bulatan"),
                    "(x-h)^2 + (y-k)^2 = r^2",
                    TranslatedText("Where (h,k) is the fixed center point and r is constant distance.", "Di mana (h,k) ialah titik pusat tetap dan r ialah jarak malar.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f4_c7_p1", Difficulty.MEDIUM,
                    TranslatedText("Find coordinates of midpoint between A(2, 4) and B(8, 10).", "Cari koordinat titik tengah antara A(2, 4) dan B(8, 10)."),
                    "5,7",
                    TranslatedText("Apply midpoint formula: ( (x1+x2)/2, (y1+y2)/2 ).", "Gunakan rumus titik tengah: ( (x1+x2)/2, (y1+y2)/2 )."),
                    TranslatedText("Midpoint = ( (2+8)/2, (4+10)/2 ) = (10/2, 14/2) = (5, 7).", "Titik Tengah = ( (2+8)/2, (4+10)/2 ) = (10/2, 14/2) = (5, 7).")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f4_c7_q1",
                    TranslatedText("If gradients of two perpendicular lines are m_1 and m_2, state their relationship.", "Jika kecerunan dua garis serenjang ialah m_1 dan m_2, nyatakan hubungan mereka."),
                    listOf("m1 * m2 = -1", "m1 = m2", "m1 * m2 = 1", "m1 - m2 = 0"),
                    0,
                    TranslatedText("Perpendicular lines have gradients that multiply to -1. Parallel lines have equal gradients.", "Garis serenjang mempunyai kecerunan yang didarab menghasilkan -1. Garis selari mempunyai kecerunan yang sama.")
                )
            )
        ),
        Chapter(
            number = 8,
            form = 4,
            name = TranslatedText("Vectors", "Vektor"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Basic Vectors and Addition", "Asas Vektor dan Penambahan"),
                    listOf(
                        TranslatedText("A vector has both magnitude and direction, unlike scalars which only have magnitude.", "Satu vektor mempunyai kedua-dua magnitud dan arah, tidak seperti skalar yang hanya mempunyai magnitud."),
                        TranslatedText("Resultant vectors can be found using Triangle, Parallelogram, or Polygon laws.", "Vektor paduan boleh dicari menggunakan hukum Segi Tiga, Segi Empat Selari, atau Poligon.")
                    )
                ),
                NoteSection(
                    TranslatedText("Vectors in Cartesian Plane", "Vektor dalam Satah Kartesius"),
                    listOf(
                        TranslatedText("Represented as xi + yj or column vector (x; y).", "Diwakili sebagai xi + yj atau vektor lajur (x; y)."),
                        TranslatedText("The unit vector represents direction with a magnitude of 1.", "Vektor unit mewakili arah dengan magnitud 1.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Magnitude of a Vector", "Magnitud Suatu Vektor"),
                    "|r| = sqrt(x^2 + y^2)",
                    TranslatedText("Derived from Pythagoras Theorem in 2D component plane.", "Diterbitkan daripada Teorem Pythagoras dalam satah komponen 2D.")
                ),
                Formula(
                    TranslatedText("Unit Vector", "Vektor Unit"),
                    "r_hat = r / |r| = (xi + yj) / sqrt(x^2 + y^2)",
                    TranslatedText("Vector divided by its magnitude yields unit vector of length 1.", "Vektor dibahagi dengan magnitudnya menghasilkan vektor unit dengan panjang 1.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f4_c8_p1", Difficulty.EASY,
                    TranslatedText("Find magnitude of vector v = 3i - 4j.", "Cari magnitud bagi vektor v = 3i - 4j."),
                    "5",
                    TranslatedText("Use magnitude formula sqrt(x^2 + y^2).", "Gunakan rumus magnitud sqrt(x^2 + y^2)."),
                    TranslatedText("|v| = sqrt(3^2 + (-4)^2) = sqrt(9 + 16) = sqrt(25) = 5.", "|v| = sqrt(3^2 + (-4)^2) = sqrt(9 + 16) = sqrt(25) = 5.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f4_c8_q1",
                    TranslatedText("Two non-zero vectors a and b are parallel if:", "Dua vektor bukan sifar a dan b adalah selari jika:"),
                    listOf("a = k * b, where k is a scalar constant", "a * b = 1", "a is perpendicular to b", "their magnitudes are equal"),
                    0,
                    TranslatedText("Vectors are parallel if and only if one is a scalar multiple of the other (a = kb).", "Vektor adalah selari jika dan hanya jika satu vektor ialah gandaan skalar vektor yang lain (a = kb).")
                )
            )
        ),
        Chapter(
            number = 9,
            form = 4,
            name = TranslatedText("Solution of Triangles", "Penyelesaian Segitiga"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Sine and Cosine Rules", "Petua Sinus dan Kosinus"),
                    listOf(
                        TranslatedText("Sine rule: a/sinA = b/sinB = c/sinC. Used when you know an angle and its opposite side.", "Petua sinus: a/sinA = b/sinB = c/sinC. Digunakan apabila anda mengetahui sudut dan sisi bertentangannya."),
                        TranslatedText("Cosine rule: a^2 = b^2 + c^2 - 2bc*cosA. Used when knowing two sides and an enclosed angle.", "Petua kosinus: a^2 = b^2 + c^2 - 2bc*cosA. Digunakan apabila mengetahui dua sisi dan satu sudut kandung.")
                    )
                ),
                NoteSection(
                    TranslatedText("Ambiguous Case & Area", "Kes Berambiguitas & Luas"),
                    listOf(
                        TranslatedText("Ambiguous case of triangles occurs when given side-side-angle (SSA) where height < a < c.", "Kes berambiguitas segi tiga berlaku apabila diberi sisi-sisi-sudut (SSA) di mana tinggi < a < c."),
                        TranslatedText("For triangle area, use Area = 0.5 * ab * sinC or Heron's Formula.", "Untuk luas segi tiga, gunakan Luas = 0.5 * ab * sinC atau Rumus Heron.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Heron's Formula", "Rumus Heron"),
                    "Area = sqrt(s(s-a)(s-b)(s-c)), where s = (a+b+c)/2",
                    TranslatedText("Calculates triangle area when only the three side lengths are known.", "Mengira luas segi tiga apabila hanya panjang tiga sisi yang diketahui.")
                ),
                Formula(
                    TranslatedText("Triangle Area (Trigonometric)", "Luas Segi Tiga (Trigonometri)"),
                    "Area = 1/2 * ab * sinC",
                    TranslatedText("Uses two sides and the sine of the angle between them.", "Menggunakan dua sisi dan sinus sudut di antara mereka.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f4_c9_p1", Difficulty.MEDIUM,
                    TranslatedText("In a triangle ABC, b = 6 cm, c = 8 cm, angle A = 30 degrees. Find Area of triangle.", "Dalam segi tiga ABC, b = 6 cm, c = 8 cm, sudut A = 30 darjah. Cari Luas segi tiga."),
                    "12",
                    TranslatedText("Apply Area = 1/2 * bc * sinA.", "Gunakan Luas = 1/2 * bc * sinA."),
                    TranslatedText("Area = 0.5 * 6 * 8 * sin(30) = 24 * 0.5 = 12 cm^2.", "Luas = 0.5 * 6 * 8 * sin(30) = 24 * 0.5 = 12 cm^2.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f4_c9_q1",
                    TranslatedText("Under what SSA conditions does an ambiguous case (two possible triangles) exist?", "Di bawah syarat SSA manakah kes berambiguitas (dua kemungkinan segi tiga) wujud?"),
                    listOf("h < a < c, where A is acute", "a < h, where A is acute", "a >= c, where A is acute", "A is obtuse and a < c"),
                    0,
                    TranslatedText("An ambiguous case exists when the opposite side 'a' is longer than active height but shorter than the adjacent side 'c' with acute angle A.", "Kes berambiguitas wujud apabila sisi bertentangan 'a' lebih panjang daripada tinggi aktif tetapi lebih pendek daripada sisi bersebelahan 'c' dengan sudut tirus A.")
                )
            )
        ),
        Chapter(
            number = 10,
            form = 4,
            name = TranslatedText("Index Numbers", "Nombor Indeks"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Price Index", "Indeks Harga"),
                    listOf(
                        TranslatedText("Measures the relative change in price compared to a base year: I = (Q1 / Q0) * 100.", "Mengukur perubahan relatif harga berbanding tahun asas: I = (Q1 / Q0) * 100."),
                        TranslatedText("Q0 is quantity/price at base time, Q1 is quantity/price at specific time.", "Q0 ialah kuantiti/harga pada masa asas, Q1 ialah kuantiti/harga pada masa tertentu.")
                    )
                ),
                NoteSection(
                    TranslatedText("Composite Index", "Indeks Gubahan"),
                    listOf(
                        TranslatedText("Aggregated index weighted by importance (weightage, W). Weighted average of individual indexes.", "Indeks agregat yang dinilai mengikut kepentingan (pemberat, W). Purata tertimbang bagi indeks individu."),
                        TranslatedText("Weightages can be represented by numbers, percentages, or charts.", "Pemberat boleh diwakili oleh nombor, peratusan, atau carta.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Price Index Formula", "Rumus Indeks Harga"),
                    "I = (Q_1 / Q_0) * 100",
                    TranslatedText("Percentage ratio of price in current year relative to base year.", "Nisbah peratusan harga pada tahun semasa berbanding tahun asas.")
                ),
                Formula(
                    TranslatedText("Composite Index Formula", "Rumus Indeks Gubahan"),
                    "I_bar = Sum(I_i * W_i) / Sum(W_i)",
                    TranslatedText("Weighted average index incorporating individual indices I_i and weightage W_i.", "Purata indeks tertimbang menggabungkan indeks individu I_i dan pemberat W_i.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f4_c10_p1", Difficulty.EASY,
                    TranslatedText("The price of a book in 2020 was RM10 and in 2022 was RM15. Find price index of book in 2022 based on 2020.", "Harga buku pada 2020 ialah RM10 dan pada 2022 ialah RM15. Cari indeks harga buku pada 2022 berasaskan 2020."),
                    "150",
                    TranslatedText("Apply I = (Q1 / Q0) * 100.", "Gunakan I = (Q1 / Q0) * 100."),
                    TranslatedText("I = (15 / 10) * 100 = 1.5 * 100 = 150.", "I = (15 / 10) * 100 = 1.5 * 100 = 150.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f4_c10_q1",
                    TranslatedText("Given three items with indexes 120, 110, 150 and weightages 3, 2, 5. Calculate composite index.", "Diberi tiga item dengan indeks 120, 110, 150 dan pemberat 3, 2, 5. Hitung indeks gubahan."),
                    listOf("133", "125", "130", "140"),
                    0,
                    TranslatedText("I_bar = (120*3 + 110*2 + 150*5) / (3+2+5) = (360 + 220 + 750) / 10 = 1330 / 10 = 133.", "I_bar = (120*3 + 110*2 + 150*5) / (3+2+5) = (360 + 220 + 750) / 10 = 1330 / 10 = 133.")
                )
            )
        ),

        // --- FORM 5 ---
        Chapter(
            number = 1,
            form = 5,
            name = TranslatedText("Circular Measure", "Sukatan Membulat"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Radian and Arc Length", "Radian dan Panjang Lengkok"),
                    listOf(
                        TranslatedText("Angle in radians: theta = (angle in degrees) * pi / 180.", "Sudut dalam radian: theta = (sudut dalam darjah) * pi / 180."),
                        TranslatedText("Arc length s = r * theta (where theta is in radians).", "Panjang lengkok s = r * theta (di mana theta adalah dalam radian).")
                    )
                ),
                NoteSection(
                    TranslatedText("Area of Sector and Segment", "Luas Sektor dan Tembereng"),
                    listOf(
                        TranslatedText("Area of sector A = 0.5 * r^2 * theta.", "Luas sektor A = 0.5 * r^2 * theta."),
                        TranslatedText("Area of segment = Area of sector - Area of triangle = 0.5 * r^2 * (theta - sin(theta)).", "Luas tembereng = Luas sektor - Luas segi tiga = 0.5 * r^2 * (theta - sin(theta)).")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Arc Length of Circle", "Panjang Lengkok Bulatan"),
                    "s = r * theta",
                    TranslatedText("theta must be in radians.", "theta mesti dalam radian.")
                ),
                Formula(
                    TranslatedText("Area of Sector of Circle", "Luas Sektor Bulatan"),
                    "A = 1/2 * r^2 * theta",
                    TranslatedText("Calculates flat sector area bounds for theta in radians.", "Mengira luas kawasan sektor rata untuk theta dalam radian.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f5_c1_p1", Difficulty.EASY,
                    TranslatedText("Find arc length of a circle of radius r = 5 cm and theta = 2 radians.", "Cari panjang lengkok bagi bulatan berjejari r = 5 cm dan theta = 2 radian."),
                    "10",
                    TranslatedText("Apply s = r * theta.", "Gunakan s = r * theta."),
                    TranslatedText("s = 5 * 2 = 10 cm.", "s = 5 * 2 = 10 cm.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f5_c1_q1",
                    TranslatedText("Convert 60 degrees into radians (in terms of pi).", "Tukarkan 60 darjah kepada radian (dalam sebutan pi)."),
                    listOf("pi / 3", "pi / 6", "2 * pi / 3", "pi / 2"),
                    0,
                    TranslatedText("60 * pi / 180 = pi / 3 radians.", "60 * pi / 180 = pi / 3 radian.")
                )
            )
        ),
        Chapter(
            number = 2,
            form = 5,
            name = TranslatedText("Differentiation", "Pembezaan"),
            notes = listOf(
                NoteSection(
                    TranslatedText("First Derivative Concepts", "Konsep Terbitan Pertama"),
                    listOf(
                        TranslatedText("Derivative is the rate of change of y with respect to x. Represented as dy/dx or f'(x).", "Terbitan ialah kadar perubahan y terhadap x. Diwakili sebagai dy/dx atau f'(x)."),
                        TranslatedText("First Principle: dy/dx = lim (delta_x->0) [f(x + delta_x) - f(x)] / delta_x.", "Prinsip Pertama: dy/dx = lim (delta_x->0) [f(x + delta_x) - f(x)] / delta_x.")
                    )
                ),
                NoteSection(
                    TranslatedText("Rules and Turning Points", "Petua dan Titik Pusingan"),
                    listOf(
                        TranslatedText("Product Rule: d(uv)/dx = u'v + uv'. Quotient Rule: d(u/v)/dx = (u'v - uv') / v^2.", "Petua Darab: d(uv)/dx = u'v + uv'. Petua Bahagi: d(u/v)/dx = (u'v - uv') / v^2."),
                        TranslatedText("At turning points, dy/dx = 0. Use second derivative d^2y/dx^2 to check nature: > 0 is min, < 0 is max.", "Pada titik pusingan, dy/dx = 0. Gunakan terbitan kedua d^2y/dx^2 untuk memeriksa jenis: > 0 ialah min, < 0 ialah maks.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Derivative of Polynomial", "Terbitan Polinomial"),
                    "d(ax^n)/dx = anx^(n-1)",
                    TranslatedText("The core power rule of basic algebraic differentiation.", "Petua kuasa teras pembezaan algebra asas.")
                ),
                Formula(
                    TranslatedText("Chain Rule", "Petua Rantai"),
                    "dy/dx = (dy/du) * (du/dx)",
                    TranslatedText("Used to differentiate composite functions.", "Digunakan untuk membezakan fungsi gubahan.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f5_c2_p1", Difficulty.MEDIUM,
                    TranslatedText("Find derivative dy/dx of y = 3x^4.", "Cari terbitan dy/dx bagi y = 3x^4."),
                    "12x^3",
                    TranslatedText("Apply power rule ax^n => anx^(n-1).", "Gunakan petua kuasa ax^n => anx^(n-1)."),
                    TranslatedText("dy/dx = 3 * 4 * x^(4-1) = 12x^3.", "dy/dx = 3 * 4 * x^(4-1) = 12x^3.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f5_c2_q1",
                    TranslatedText("If d^2y/dx^2 is negative at a station point, then the point is a:", "Jika d^2y/dx^2 adalah negatif pada satu titik pegun, maka titik itu ialah:"),
                    listOf("Maximum point", "Minimum point", "Point of inflection", "Stationary loop"),
                    0,
                    TranslatedText("A negative second derivative indicates a maximum turning point. Positive indicates a minimum point.", "Terbitan kedua yang negatif menunjukkan titik pusingan maksimum. Positif menunjukkan titik minimum.")
                )
            )
        ),
        Chapter(
            number = 3,
            form = 5,
            name = TranslatedText("Integration", "Pengamiran"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Indefinite Integral", "Kamilan Tak Tentu"),
                    listOf(
                        TranslatedText("Integration is the reverse process of differentiation. Always add the constant '+ C'.", "Pengamiran ialah proses songsang bagi pembezaan. Sentiasa tambah pemalar '+ C'."),
                        TranslatedText("Integral of x^n dx = [x^(n+1) / (n+1)] + C (for n != -1).", "Kamilan bagi x^n dx = [x^(n+1) / (n+1)] + C (untuk n != -1).")
                    )
                ),
                NoteSection(
                    TranslatedText("Definite Integral Applicatons", "Aplikasi Kamilan Tentu"),
                    listOf(
                        TranslatedText("Definite integration evaluates area under a curve between interval bounds [a, b].", "Kamilan tentu menilai luas di bawah lengkung antara batas selang [a, b]."),
                        TranslatedText("Volume of revolution around x-axis: V = pi * integral [y^2] dx.", "Isi padu kisaran pada paksi-x: V = pi * kamilan [y^2] dx.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Area Under a Curve (x-axis)", "Luas di Bawah Lengkung (paksi-x)"),
                    "Area = integral_a^b (y) dx",
                    TranslatedText("Calculates the flat enclosed area bounded by the curve and the x-axis.", "Mengira luas kawasan rata tertutup yang disempadani oleh lengkung dan paksi-x.")
                ),
                Formula(
                    TranslatedText("Volume of Revolution (x-axis)", "Isi Padu Kisaran (paksi-x)"),
                    "Volume = pi * integral_a^b (y^2) dx",
                    TranslatedText("Calculates the solid 3D volume obtained by rotating a vertical area 360 degrees.", "Mengira isi padu pepejal 3D yang diperoleh dengan memutar luas menegak 360 darjah.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f5_c3_p1", Difficulty.MEDIUM,
                    TranslatedText("Evaluate the integral: integral of (3x^2) dx from x=1 to x=2.", "Nilaikan kamilan: kamilan bagi (3x^2) dx dari x=1 ke x=2."),
                    "7",
                    TranslatedText("Find anti-derivative x^3, then evaluate at bounds F(2) - F(1).", "Cari anti-terbitan x^3, kemudian nilaikan pada batas F(2) - F(1)."),
                    TranslatedText("Integral of 3x^2 is x^3. [x^3]_1^2 = 2^3 - 1^3 = 8 - 1 = 7.", "Kamilan bagi 3x^2 ialah x^3. [x^3]_1^2 = 2^3 - 1^3 = 8 - 1 = 7.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f5_c3_q1",
                    TranslatedText("What is the indefinite integral of 5 dx?", "Apakah kamilan tak tentu bagi 5 dx?"),
                    listOf("5x + C", "5 + C", "5x^2 + C", "x^5 + C"),
                    0,
                    TranslatedText("The integration of a constant k yields kx + C. Thus, integral of 5 dx is 5x + C.", "Pengamiran pemalar k menghasilkan kx + C. Oleh itu, kamilan bagi 5 dx ialah 5x + C.")
                )
            )
        ),
        Chapter(
            number = 4,
            form = 5,
            name = TranslatedText("Permutation and Combination", "Pilih Atur dan Gabungan"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Multiplication Rule", "Petua Pendaraban"),
                    listOf(
                        TranslatedText("If event A occurs in m ways and event B occurs in n ways, both occur in m * n ways.", "Jika peristiwa A berlaku dalam m cara dan peristiwa B berlaku dalam n cara, kedua-dua berlaku dalam m * n cara."),
                        TranslatedText("Fundamental counting principle for sequencing.", "Prinsip membilang asas untuk susunan.")
                    )
                ),
                NoteSection(
                    TranslatedText("Permutation vs Combination", "Pilih Atur vs Gabungan"),
                    listOf(
                        TranslatedText("Permutations (nPr): Order matters! Arranging objects.", "Pilih atur (nPr): Susunan penting! Menyusun objek."),
                        TranslatedText("Combinations (nCr): Order does not matter! Selecting objects.", "Gabungan (nCr): Susunan TIDAK penting! Memilih objek.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Permutation Formula", "Rumus Pilih Atur"),
                    "nPr = n! / (n - r)!",
                    TranslatedText("Arranging r distinct objects chosen from a pool of n objects.", "Menyusun r objek berbeza yang dipilih daripada kumpulan n objek.")
                ),
                Formula(
                    TranslatedText("Combination Formula", "Rumus Gabungan"),
                    "nCr = n! / [r! * (n - r)!]",
                    TranslatedText("Selecting r objects from a pool of n distinct objects.", "Memilih r objek daripada kumpulan n objek berbeza.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f5_c4_p1", Difficulty.EASY,
                    TranslatedText("Calculate 5P3.", "Hitungkan 5P3."),
                    "60",
                    TranslatedText("Apply 5! / (5-3)!.", "Gunakan 5! / (5-3)!."),
                    TranslatedText("5P3 = 5! / 2! = (120) / 2 = 60.", "5P3 = 5! / 2! = (120) / 2 = 60.")
                ),
                PracticeQuestion(
                    "f5_c4_p2", Difficulty.EASY,
                    TranslatedText("Calculate 5C3.", "Hitungkan 5C3."),
                    "10",
                    TranslatedText("Apply 5! / (3! * 2!).", "Gunakan 5! / (3! * 2!)."),
                    TranslatedText("5C3 = 120 / (6 * 2) = 120 / 12 = 10.", "5C3 = 120 / (6 * 2) = 120 / 12 = 10.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f5_c4_q1",
                    TranslatedText("How many ways can we select a committee of 3 members from a group of 8 people?", "Berapakah bilangan cara untuk memilih jawatankuasa berkumpulan 3 orang daripada kumpulan 8 orang?"),
                    listOf("56", "336", "24", "48"),
                    0,
                    TranslatedText("Selection order does not matter (Committee). Use 8C3 = 8! / (3! * 5!) = (8*7*6)/(3*2*1) = 56.", "Susunan pemilihan tidak penting (Jawatankuasa). Gunakan 8C3 = 8! / (3! * 5!) = (8*7*6)/(3*2*1) = 56.")
                )
            )
        ),
        Chapter(
            number = 5,
            form = 5,
            name = TranslatedText("Probability Distribution", "Taburan Kebarangkalian"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Random Variables", "Pemboleh Ubah Rawak"),
                    listOf(
                        TranslatedText("Discrete random variables have countable distinct outcomes (e.g. number of heads).", "Pemboleh ubah rawak diskret mempunyai hasil berasingan yang boleh dibilang (cth. bilangan kepala)."),
                        TranslatedText("Continuous random variables are measurable values (e.g. student heights).", "Pemboleh ubah rawak selanjar pula merupakan nilai yang boleh diukur (cth. tinggi murid).")
                    )
                ),
                NoteSection(
                    TranslatedText("Binomial and Normal", "Binomial dan Normal"),
                    listOf(
                        TranslatedText("Binomial distribution acts on Bernoulli trials (success/failure, fixed n trials).", "Taburan binomial bertindak ke atas cubaan Bernoulli (jaya/gagal, n cubaan tetap)."),
                        TranslatedText("Normal distribution is continuous, symmetric bell curve about mean.", "Taburan normal ialah taburan selanjar, lengkung loceng simetri sekitar min.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Binomial Probability", "Kebarangkalian Binomial"),
                    "P(X = r) = nCr * p^r * q^(n-r), where q = 1 - p",
                    TranslatedText("p is success probability, q is failure probability.", "p ialah kebarangkalian kejayaan, q ialah kebarangkalian kegagalan.")
                ),
                Formula(
                    TranslatedText("Standard Normal score (Z)", "Skor Normal Piawai (Z)"),
                    "Z = (X - mu) / sigma",
                    TranslatedText("Converts a normal value X into a standardized score of mean 0, variance 1.", "Menukarkan nilai normal X kepada skor terpiawai dengan min 0, varians 1.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f5_c5_p1", Difficulty.MEDIUM,
                    TranslatedText("A coin is flipped 3 times. What is probability of getting exactly 2 heads? (p = 0.5)", "Satu syiling dilambung 3 kali. Apakah kebarangkalian mendapat tepat 2 kepala? (p = 0.5)"),
                    "0.375",
                    TranslatedText("Use Binomial formula with n=3, r=2, p=0.5, q=0.5.", "Gunakan rumus Binomial dengan n=3, r=2, p=0.5, q=0.5."),
                    TranslatedText("P(X = 2) = 3C2 * (0.5)^2 * (0.5)^1 = 3 * 0.25 * 0.5 = 0.375.", "P(X = 2) = 3C2 * (0.5)^2 * (0.5)^1 = 3 * 0.25 * 0.5 = 0.375.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f5_c5_q1",
                    TranslatedText("Convert a raw score X = 80 into Z-score when mean is 70 and standard deviation is 5.", "Tukarkan skor mentah X = 80 kepada skor-Z apabila min ialah 70 dan sisihan piawai ialah 5."),
                    listOf("2.0", "1.5", "10", "2.5"),
                    0,
                    TranslatedText("Z = (X - mu) / sigma = (80 - 70) / 5 = 10 / 5 = 2.0.", "Z = (X - mu) / sigma = (80 - 70) / 5 = 10 / 5 = 2.0.")
                )
            )
        ),
        Chapter(
            number = 6,
            form = 5,
            name = TranslatedText("Trigonometric Functions", "Fungsi Trigonometri"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Graphs and Ratios", "Graf dan Nisbah"),
                    listOf(
                        TranslatedText("Reciprocals: cosecC = 1/sinC, secC = 1/cosC, cotC = 1/tanC.", "Salingan: cosecC = 1/sinC, secC = 1/cosC, cotC = 1/tanC."),
                        TranslatedText("Verify trigonometric values using unit circle or right angles.", "Sahkan nilai trigonometri menggunakan bulatan unit atau sudut tegak.")
                    )
                ),
                NoteSection(
                    TranslatedText("Addition and Double Angle Formulae", "Rumus Tambahan dan Sudut Berganda"),
                    listOf(
                        TranslatedText("Provide composite angles solution: sin(A +- B) = sinA*cosB +- cosA*sinB.", "Sediakan penyelesaian sudut gubahan: sin(A +- B) = sinA*cosB +- cosA*sinB."),
                        TranslatedText("Useful for proving identities or solving trig equations.", "Berguna untuk membuktikan identiti atau menyelesaikan persamaan trigonometri.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Pythagorean Identity", "Identiti Pythagoras"),
                    "sin^2(x) + cos^2(x) = 1",
                    TranslatedText("Base trigonometric relationship for any angle x.", "Hubungan trigonometri asas bagi sebarang sudut x.")
                ),
                Formula(
                    TranslatedText("Double Angle for Sine", "Sudut Berganda bagi Sinus"),
                    "sin(2A) = 2*sinA*cosA",
                    TranslatedText("Derived from the addition formula for sin(A + B).", "Diterbitkan daripada rumus penambahan bagi sin(A + B).")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f5_c6_p1", Difficulty.MEDIUM,
                    TranslatedText("Find value of cos(90 - theta) if sin(theta) = 0.6.", "Cari nilai cos(90 - theta) jika sin(theta) = 0.6."),
                    "0.6",
                    TranslatedText("Complementary Angle relations state cos(90 - theta) = sin(theta).", "Hubungan Sudut Pelengkap menyatakan cos(90 - theta) = sin(theta)."),
                    TranslatedText("Thus, cos(90 - theta) = sin(theta) = 0.6.", "Oleh itu, cos(90 - theta) = sin(theta) = 0.6.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f5_c6_q1",
                    TranslatedText("Which of the following is equivalent to 1 + tan^2(x)?", "Antara berikut, yang manakah setara dengan 1 + tan^2(x)?"),
                    listOf("sec^2(x)", "cosec^2(x)", "cos^2(x)", "cot^2(x)"),
                    0,
                    TranslatedText("One of the core basic identities: 1 + tan^2(x) = sec^2(x).", "Salah satu identiti asas teras: 1 + tan^2(x) = sec^2(x).")
                )
            )
        ),
        Chapter(
            number = 7,
            form = 5,
            name = TranslatedText("Linear Programming", "Pengaturcaraan Linear"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Formulating Models", "Membina Model"),
                    listOf(
                        TranslatedText("Express verbal constraints as linear inequalities (e.g. 'y is at least 3x' => y >= 3x).", "Nyatakan kekangan lisan sebagai ketaksamaan linear (cth. 'y sekurang-kurangnya 3x' => y >= 3x)."),
                        TranslatedText("Determine the bounded region (feasible region) that satisfies all constraints.", "Tentukan rantau berlorek (rantau boleh laksana) yang memenuhi semua kekangan.")
                    )
                ),
                NoteSection(
                    TranslatedText("Optimization", "Pengoptimuman"),
                    listOf(
                        TranslatedText("The vertex principle states optimum objective values occur at boundary vertices of the feasible region.", "Prinsip verteks menyatakan nilai objektif optimum berlaku pada verteks sempadan rantau laksana."),
                        TranslatedText("Plot parameter lines representing objective function to determine optimal point.", "Plot garis parameter mewakili fungsi objektif untuk menentukan titik optimum.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Objective Function Form", "Bentuk Fungsi Objektif"),
                    "Max/Min P = Ax + By",
                    TranslatedText("The profit or cost function we want to maximize or minimize.", "Fungsi keuntungan atau kos yang ingin kita maksimumkan atau minimumkan.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f5_c7_p1", Difficulty.MEDIUM,
                    TranslatedText("Write constraint inequality: 'The number of teachers (y) must be at least twice the number of students (x)'.", "Tulis ketaksamaan kekangan: 'Bilangan guru (y) mestilah sekurang-kurangnya dua kali ganda bilangan murid (x)'."),
                    "y >= 2x",
                    TranslatedText("Translate 'at least' to >= sign.", "Terjemahkan 'sekurang-kurangnya' kepada tanda >=."),
                    TranslatedText("Teacher y >= 2 * Student x, so y >= 2x.", "Guru y >= 2 * Murid x, maka y >= 2x.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f5_c7_q1",
                    TranslatedText("In linear programming, where does the optimum value of the objective function always lie?", "Dalam pengaturcaraan linear, di manakah nilai optimum bagi fungsi objektif sentiasa terletak?"),
                    listOf("At one of the vertices of the feasible region", "In the exact center of feasible region", "On the y-intercept", "At the origin (0, 0)"),
                    0,
                    TranslatedText("By the Optimal Vertex Theorem, the maximum or minimum of a linear objective function occurs at the boundary vertices.", "Mengikut Teorem Verteks Optimal, nilai maksimum atau minimum bagi fungsi objektif linear berlaku pada bucu-bucu sempadan.")
                )
            )
        ),
        Chapter(
            number = 8,
            form = 5,
            name = TranslatedText("Kinematics of Linear Motion", "Kinematik Gerakan Linear"),
            notes = listOf(
                NoteSection(
                    TranslatedText("Displacement, Velocity, and Acceleration", "Sesaran, Halaju, dan Pecutan"),
                    listOf(
                        TranslatedText("Displacement (s) represents position relative to initial point O.", "Sesaran (s) mewakili kedudukan relatif kepada titik mula O."),
                        TranslatedText("Velocity (v) is rate of change of displacement v = ds/dt.", "Halaju (v) ialah kadar perubahan sesaran v = ds/dt."),
                        TranslatedText("Acceleration (a) is rate of change of velocity a = dv/dt = d^2s/dt^2.", "Pecutan (a) ialah kadar perubahan halaju a = dv/dt = d^2s/dt^2.")
                    )
                ),
                NoteSection(
                    TranslatedText("Integration in Motion", "Pengamiran dalam Gerakan"),
                    listOf(
                        TranslatedText("To get velocity from acceleration: v = integral(a) dt.", "Untuk mendapatkan halaju daripada pecutan: v = kamilan(a) dt."),
                        TranslatedText("To get displacement from velocity: s = integral(v) dt.", "Untuk mendapatkan sesaran daripada halaju: s = kamilan(v) dt.")
                    )
                )
            ),
            formulas = listOf(
                Formula(
                    TranslatedText("Velocity Derivative", "Terbitan Halaju"),
                    "v = ds / dt",
                    TranslatedText("Instantaneous rate of change of displacement.", "Kadar perubahan sesaran seketika.")
                ),
                Formula(
                    TranslatedText("Acceleration Derivative", "Terbitan Pecutan"),
                    "a = dv / dt = d^2s / dt^2",
                    TranslatedText("Instantaneous rate of change of velocity.", "Kadar perubahan halaju seketika.")
                )
            ),
            practiceQuestions = listOf(
                PracticeQuestion(
                    "f5_c8_p1", Difficulty.MEDIUM,
                    TranslatedText("A particle's displacement is s = 2t^3. Find its velocity at time t = 2.", "Sesaran suatu zarah diberi oleh s = 2t^3. Cari halajunya pada masa t = 2."),
                    "24",
                    TranslatedText("Differentiate s to get v = ds/dt, then substitute t = 2.", "Bezakankan s untuk mendapatkan v = ds/dt, kemudian gantikan t = 2."),
                    TranslatedText("v = ds/dt = 6t^2. At t = 2: v = 6 * (2^2) = 24 m/s.", "v = ds/dt = 6t^2. Pada t = 2: v = 6 * (2^2) = 24 m/s.")
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    "f5_c8_q1",
                    TranslatedText("What is the initial velocity of a particle when s = t^2 - 4t + 3?", "Apakah halaju awal bagi suatu zarah apabila s = t^2 - 4t + 3?"),
                    listOf("-4", "3", "0", "2"),
                    0,
                    TranslatedText("v = ds/dt = 2t - 4. Initial velocity is at t = 0: v = 2(0) - 4 = -4.", "v = ds/dt = 2t - 4. Halaju awal ialah pada t = 0: v = 2(0) - 4 = -4.")
                )
            )
        )
    )
}
