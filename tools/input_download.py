import sys
import requests

YEAR = 2021

def main():
	args = sys.argv
	if len(args) != 3:
		print(f"Please provide a session cookie and a day:\n{args[0]} [cookie] [day]")
		sys.exit(1)
	
	cookie = args[1]
	day = args[2]

	url = f"https://adventofcode.com/{YEAR}/day/{day}/input"

	session = requests.Session()
	response = session.get(url, cookies = {'session': cookie})

	with open(f"../inputs/{YEAR}/day_{day}.txt", "w") as f:
		f.write(response.text)



if __name__ == '__main__':
	main()