import sys
import requests
import os

AOC_COOKIE = os.environ["AOC_COOKIE"]
YEAR = 2021
DEST_FOLDER="inputs"
CODE_LOCATION="src/main/kotlin"

def main():
	if not AOC_COOKIE:
		# You can get a session cookie by looking at the "Application/Cookies" tab in Chrome Dev Tools.
		print(f"AOC_COOKIE not defined as an environment variable")
		sys.exit(1)

	args = sys.argv
	if len(args) != 2:
		print(f"Please provide a day:\n{args[0]} [day]")
		sys.exit(1)
	
	day = args[1]

	url = f"https://adventofcode.com/{YEAR}/day/{day}/input"

	session = requests.Session()
	response = session.get(url, cookies = {'session': AOC_COOKIE})

	full_path = f"{DEST_FOLDER}/{YEAR}/day_{day}.txt"
	with open(full_path, "w") as f:
		f.write(response.text)
		print(f"Wrote to: {full_path}")

	# Create a placeholder for the demo input
	full_path = f"{DEST_FOLDER}/{YEAR}/day_{day}_demo.txt"
	with open(full_path, "w") as f:
		pass

	# Create placeholder kt by copying Day0.kt
	template_path = f"{CODE_LOCATION}/Day0.kt"

	if os.path.isfile(template_path):
		template_content = ""
		with open(template_path, "r") as f:
			template_content = f.read()

		template_dest = f"{CODE_LOCATION}/Day{day}.kt"
		if not os.path.isfile(template_dest):
			with open(template_dest, "w") as f:
				template_content = template_content.replace("day0", f"day{day}")
				template_content = template_content.replace("day_0", f"day_{day}")
				f.write(template_content)






if __name__ == '__main__':
	main()