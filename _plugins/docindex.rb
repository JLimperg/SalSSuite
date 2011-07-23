# Copyright (c) 2011 Jannis Limperg <jannis.limperg@arcor.de>

# Permission is hereby granted, free of charge, to any person
# obtaining a copy of this software and associated documentation
# files (the "Software"), to deal in the Software without restriction,
# including without limitation the rights to use, copy, modify, merge,
# publish, distribute, sublicense, and/or sell copies of the Software,
# and to permit persons to whom the Software is furnished to do so,
# subject to the following conditions:

# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.

# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
# IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
# CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
# TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
# SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

module Jekyll


    # Generates index files for every version of the documentation
    # located in doc/X.Y.Z/. An index file is a simple list of
    # links to all jekyll pages that define the 'version'
    # variable in their YAML front matter. For each version
    # found in the various files, a new index file is
    # generated. The files are located in subfolders of
    # doc/ which correspond to the version strings
    # (f.ex. doc/1.0.x/).
    #
    # NOTE that automatic regeneration (--auto)
    # seems not to work properly with this
    # Generator.
    class Docpage_Index_Generator < Generator
        
        # Generates the index files.
        #
        # site - the jekyll site for which index pages
        #        should be generated
        def generate(site)
            @site = site
            determine_doc_pages
            generate_index
        end
        
        # Creates a map of all documentation versions and their
        # associated versions currently present in any of the
        # doc/X.Y.Z/ folders. The files must define the 'version'
        # variable in their YAML front matter, preferably matching
        # the version number indicated by the folder they are in. ;)
        #
        # The map (@versions) will contain the version strings
        # (f.ex. "1.0.x") as keys and for every version string an
        # array of page objects which belong to that version.
        # @versions may be nil if there are no pages which define
        # the 'version' variable.
        def determine_doc_pages
            @site.pages.each do |page|
                # Check if the page is located in a subfolder of
                # doc/
                if page.destination("/") =~ /\/doc\/.*\.html/
                    # Extract the version
                    version = page.data['version']
                    if version == nil
                        next
                    end
                    
                    # Insert and page, and the version if necessary, into the map
                    if @versions == nil
                        @versions = {version => [page]}
                    else
                        @versions[version] == nil ? @versions[version] = [page] : @versions[version] << page
                    end
                end
            end
        end
        
        # Generates index files according to the versions map created by
        # determine_doc_pages. For every version (i.e. key in the map)
        # file is created which contains links to all the pages
        # stored in the array @versions[version].
        #
        # This function creates a temporary file named 'pages.markdown'
        # which is then rendered using the layout 'docindex'.
        def generate_index
            # The following asserts that @versions is not nil, which
            # means there are no pages with the 'version: xxx' YAML variable.
            if @versions == nil
                return
            end
            
            # One index file per version:
            @versions.keys.each do |version|
                indexfile = "#{@site.source}/doc/#{version}/pages.markdown"
                file = File.new(indexfile, "w")
                file.puts("---\nlayout: docindex\n")
                file.puts("title: Uebersicht (#{version})\n---")
                file.puts("# &Uuml;bersicht - Version #{version}")
                
                # 'links' will contain all the markdown links
                # which we are going to write to the file.
                links = ""
                
                # One link per page that belongs to the version:
                @versions[version].each do |page|                
                    # The following takes the first line of the file
                    # and removes a preceding "# " if present
                    # (Markdown sign for <h1>). This line is then
                    # used as the link title.
                    begin
                        title = page.content.match(/# (.*)\n.*/)[1]
                    rescue
                        title = page.content.match(/(.*)\n.*/)[1]
                    end
                    url = page.url.match(/([^\/]*$)/)[1]
                    
                    # index.html should appear on top of all the page
                    # links.
                    if url != "index.html"
                        links += "* [#{title}](#{url})  \n"
                    else
                        file.puts("[Startseite](#{url})\n\n")
                    end
                end
                
                file.puts(links)
                file.close
              
                # Render the new page
                indexpage = Jekyll::Page.new(@site, @site.source, "doc/#{version}/", "pages.markdown")
                indexpage.render(@site.layouts, @site.site_payload)
                indexpage.write(@site.dest)
                @site.pages << indexpage
            end
        end 
    end
end
