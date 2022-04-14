from urllib.request import urlopen
import re
import sys
import math

count = int(sys.argv[1:][0])


def twofifty(movies, moviesList, want):
    counter = 0
    f = open("scraped_movies.csv", "a", encoding="utf-8")

    while counter < want:
        currentMovie = []
        # find separate movies
        movieStart = movies.find('<div class="lister-item mode-advanced">')
        tmp = movies[movieStart:]
        movieEnd = tmp.find('<p class="sort-num_votes-visible">')
        movie = tmp[:movieEnd]

        imgStart = movie.find('loadlate="')+10
        tmp = movie[imgStart:]
        imgStop = tmp.find(".jpg\"")+4
        img = tmp[:imgStop]
        if(img[:8] != "https://" or '"' in img):
            counter += 1
            movies = movies[movieEnd:stopindex]
            continue

        headerStart = movie.find('<h3 class="lister-item-header">')
        headerStop = movie.find('</h3>')
        header = movie[headerStart:headerStop]
        titleStart = (header.find('<a href="') + 9)
        temp = header[titleStart:]
        titleStart = temp.find(">") + 1
        temp = temp[titleStart:]
        titleEnd = temp.find("</a>")
        title = temp[:titleEnd]

        yearStart = header.find('unbold">(') + 9
        year = -1
        if (yearStart != 8):
            if (not header[yearStart].isnumeric()):
                temp = header[yearStart - 1:]
                parenthesisEnd = temp.find(")")
                title += " " + temp[:parenthesisEnd + 1]
                year = temp[parenthesisEnd + 3:parenthesisEnd + 7]
            else:
                year = header[yearStart:yearStart + 4]
        if (year == -1 or year == "span"):
            counter += 1
            movies = movies[movieEnd:stopindex]
            continue

        infoStart = movie.find('<p class="text-muted ">')
        infoStop = movie.find('</p>')
        info = movie[infoStart:infoStop]
        runtime = info.find('runtime">') + 9
        duration = -1
        if runtime != 8:
            runtime = info[runtime:]
            runtimeEnd = runtime.find('min</')
            runtime = runtime[:runtimeEnd - 1]
            duration = runtime
        if (duration == -1):
            counter += 1
            movies = movies[movieEnd:stopindex]
            continue

        genre = info.find('genre">') + 8
        if (genre != 7):
            genre = info[genre:]
            genreEnd = genre.find('</span>')
            genre = genre[:genreEnd - 1].strip()
            genre = genre.split(", ")
        else:
            genre = []

        ratingStart = movie.find('imdb-rating"></span>') + 37
        rating = -1
        if ratingStart != 36:
            temp = movie[ratingStart:]
            ratingEnd = temp.find('</strong>')
            rating = temp[:ratingEnd]
        if(rating == -1):
            counter += 1
            movies = movies[movieEnd:stopindex]
            continue

        descriptionStart = movie.find('text-muted">') + 13
        temp = movie[descriptionStart:]
        descriptionEnd = temp.find('</p>')
        description = temp[:descriptionEnd]
        description = re.sub(r'<a href.*\">', '', description)
        description = re.sub(r'<\/a>', '', description)
        if ("See full summary" in description or "Add a Plot" in description or "See full synopsis" in description):
            movies = movies[movieEnd:stopindex]
            counter += 1
            continue

        directorStart = movie.find('Director:') + 9
        directors = []
        if directorStart != 8:
            temp = movie[directorStart:]
            directorStart = temp.find(">") + 1
            temp = temp[directorStart:]
            directorEnd = temp.find('</a>')
            director = temp[:directorEnd]
            directors.append(director)
        else:
            directorsStart = movie.find('Directors:') + 9
            if (directorsStart != 8):
                temp = movie[directorsStart:]
                directorsStop = temp.find('<span class="ghost">|</span>')
                temp = temp[:directorsStop].strip()
                temp = temp.split("</a>")
                for i in range(0, len(temp) - 1):
                    directorStart = temp[i].find(">") + 1
                    if "<" not in temp[i][directorStart:]:
                        directors.append(temp[i][directorStart:])

        starsStart = movie.find('Stars:') + 6
        starsList = []
        if (starsStart != 5):
            temp = movie[starsStart:]
            starsStop = temp.find("</p>")
            temp = temp[:starsStop].strip()
            temp = temp.split("</a>")
            starsList = []
            for i in range(0, len(temp) - 1):
                starStart = temp[i].find(">") + 1
                starsList.append(temp[i][starStart:])

        movies = movies[movieEnd:stopindex]
        counter += 1
        currentMovie.append([img, title, year, genre, rating, duration, description, directors, starsList])
        if (currentMovie not in moviesList):
            f.write(
                str(img) + "|" + str(title) + "|" + str(year) + "|" + str(genre) + "|" + str(rating) + "|" + str(duration) + "|" + str(
                    description) + "|" + str(directors) + "|" + str(starsList) + "\n")
            moviesList.append(currentMovie)
    f.close()


pagesNeeded = math.ceil(count / 250)
f = open("scraped_movies.csv", "w")
f.write("img|title|year|genre|rating|duration|description|directors|starsList\n")
f.close()
moviesList = []
for i in range(0, pagesNeeded):
    if i == 0:
        url = "https://www.imdb.com/search/title/?title_type=feature,tv_movie,short&count=250&start=" + str(
            1 + (i * 250))
    print(url)
    want = -1
    if count > 250:
        count -= 250
        want = 250
    else:
        want = count

    page = urlopen(url)
    html_bytes = page.read()
    html = html_bytes.decode("utf-8")
    startindex = html.find('<div class="lister-list">')
    html = html[startindex:]
    stopindex = html.find('<div class="desc">')
    movies = html[:stopindex]
    temp = html[stopindex:]
    nextFind = temp.find(
        '<span class="ghost">|</span>        <a href="/search/title/?title_type=feature,tv_movie,short&')
    temp = temp[nextFind:]
    if i == 0:
        findUrl = temp.find('<a href="') + 9
        findEOF = temp.find('\n') - 1
        nextUrl = "https://www.imdb.com" + temp[findUrl:findEOF]
    else:
        findUrl = temp.find('/search/title/?title_type=feature,tv_movie,short&count=250&after=')
        if findUrl < 0:
            findUrl = temp.find('<a href="') + 9
            temp = temp[findUrl:]
            findUrl = temp.find('<a href="') + 9
            temp = temp[findUrl:]
            findEOF = temp.find('\n') - 1
            nextUrl = "https://www.imdb.com" + temp[:findEOF]
        else:
            temp = temp[findUrl:]
            findEOF = temp.find('\n') - 1
            nextUrl = "https://www.imdb.com" + temp[:findEOF]
    url = nextUrl
    twofifty(movies, moviesList, want)

print(len(moviesList))