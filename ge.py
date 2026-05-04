import requests as req
import json

CLASS_NAMES = """Writing_and_Information
Visual_and_Performing_Arts
Historical_and_Cultural_Studies
Social_and_Behavioral_Sciences
Race_Ethnic_and_Gender_Diversity
Theme_Citizenship_for_a_Diverse_and_Just_World
All_Other_Themes""".split("\n")

# Corresponding links used for API calls
GE_LINKS = ['GEN%20Foundation:%20Writing%20and%20Information%20Literacy',
            'GEN%20Foundation:%20Literary,%20Visual%20%26%20Performing%20Arts',
            'GEN%20Foundation:%20Historical%20and%20Cultural%20Studies',
            'GEN%20Foundation:%20Social%20and%20Behavioral%20Sciences',
            'GEN%20Foundation:%20Race,%20Ethnicity%20%26%20Gender%20Diversity',
            'GEN%20Theme:%20Citizenship%20for%20a%20Diverse%20%26%20Just%20World',
            ['GEN%20Theme:%20Health%20and%20Well-being', 'GEN%20Theme:%20Lived%20Environments',
             'GEN%20Theme:%20Sustainability', 'GEN%20Theme:%20Migration,%20Mobility,%20and%20Immobility',
             'GEN%20Theme:%20Origins%20and%20Evolution', 'GEN%20Theme:%20Number,%20Nature,%20Mind']]


def request(page, url):
  return json.loads(req.get(f"https://content.osu.edu/v2/classes/search?q=&client=class-search-ui&campus=col&p={page}&gen-categories={url}").text)

file = open("ge.txt", "w")



for i in range(len(CLASS_NAMES)):
  page = 1
  data = request(page, GE_LINKS[i])
  file.write(f"\n{CLASS_NAMES[i]}\n\n")

  for course in data['data']['courses']:
      file.write(f"{course['course']['subject']} {course['course']['catalogNumber']}\n")
      file.write(f"{course['course']['description']}\n")

  while (data['data']['nextPageLink'] is not None):
    print(page)
    for course in data['data']['courses']:
      file.write(f"{course['course']['subject']} {course['course']['catalogNumber']}\n")
      file.write(f"{course['course']['description']}\n")
    page = page + 1
    data = request(page, GE_LINKS[i])

file.close()

